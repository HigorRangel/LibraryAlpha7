package swing.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import swing.annotations.CsvProperties;
import swing.converters.DefaultConverter;
import swing.interfaces.CSVConverter;
import swing.interfaces.CSVImportable;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Classe utilitária para manipulação de arquivos CSV.
 */
public class CSVUtils {

    private CSVUtils() {
    }

    /**
     * Método para converter um CSV em um objeto.
     *
     * @param reader um Reader contendo o CSV a ser convertido.
     * @param <T>    o tipo do objeto a ser retornado.
     * @return um objeto do tipo T, preenchido com os dados do CSV.
     * @throws IOException se ocorrer um error ao ler o CSV.
     */
    public static <T extends CSVImportable> Set<T> csvToObject(Reader reader, Class<T> clazz) throws IOException {
        CSVFormat format = CSVFormat.DEFAULT
                .builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .build();

        try (CSVParser parser = new CSVParser(reader, format)) {
            if (parser.getHeaderNames().isEmpty()) {

                throw new IllegalArgumentException("O CSV não contém cabeçalhos válidos.");
            }
            validateHeaders(parser, clazz);

            List<Field> fields = getClassFields(clazz);
            Set<T> objects = new LinkedHashSet<>();

            for (CSVRecord csvRecord : parser) {
                T object = instanceNewObject(clazz);
                for (Field field : fields) {
                    if (field.isAnnotationPresent(CsvProperties.class)) {
                        CsvProperties propriedades = field.getAnnotation(CsvProperties.class);
                        String headerName = field.getName();
                        if (csvRecord.isMapped(headerName)) {
                            String value = csvRecord.get(headerName);
                            field.setAccessible(true);
                            setObjectValue(csvRecord, field, value, propriedades, object, headerName);
                        }
                    }
                }
                objects.add(object);
            }
            return objects;
        }
    }

    /**
     * Método auxiliar para instanciar um novo objeto do tipo ImportavelCSV.
     *
     * @param clazz a classe do objeto que será instanciado.
     * @param <T>   o tipo do objeto ImportavelCSV que está sendo instanciado.
     * @return um novo objeto do tipo ImportavelCSV.
     */
    private static <T extends CSVImportable> T instanceNewObject(Class<T> clazz) {
        T object;
        try {
            object = clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new IllegalArgumentException("Erro ao instanciar a classe " + clazz.getName() + ": " + e.getMessage(), e);
        }
        return object;
    }

    /**
     * Método auxiliar para definir o valor de um campo em um objeto ImportavelCSV.
     *
     * @param csvRecord  o registro CSV atual.
     * @param field      o campo do objeto ImportavelCSV que será preenchido.
     * @param value      o valor a ser definido no campo.
     * @param properties as properties do campo anotadas com @PropriedadesCSV.
     * @param object     o objeto ImportavelCSV que será preenchido.
     * @param headerName o nome do cabeçalho relacionado ao campo.
     * @param <T>        o tipo do objeto ImportavelCSV que está sendo preenchido.
     */
    private static <T extends CSVImportable> void setObjectValue(CSVRecord csvRecord
            , Field field, String value, CsvProperties properties, T object, String headerName) {
        if (value != null && !value.isEmpty()) {
            CSVConverter<?> converter = null;
            try {
                converter = properties.csvConverter().getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                addErrorMessage(object, String.format("Erro ao instanciar o csvConverter %s. Erro: %s",
                        properties.csvConverter().getName(), e.getMessage()), csvRecord, headerName);
            }
            Object convertedValue;
            if (converter instanceof DefaultConverter) {
                convertedValue = convertStringToType(value, field.getType());
            } else if (converter != null) {
                convertedValue = converter.converter(value);
            } else {
                convertedValue = value;
            }
            try {
                field.set(object, convertedValue);
            } catch (IllegalAccessException e) {
                addErrorMessage(object, String.format("Erro ao definir o valor %s. Erro: %s", value,
                        e.getMessage()), csvRecord, headerName);
            }
        } else if (properties.required()) {
            addErrorMessage(object, String.format("O campo %s é obrigatório e não pode ser nulo ou vazio.",
                    headerName), csvRecord, headerName);
        }
    }

    /**
     * Método auxiliar para adicionar uma message de error a um objeto ImportavelCSV.
     *
     * @param object     o objeto ImportavelCSV ao qual a message de error será adicionada.
     * @param message    a message de error a ser adicionada.
     * @param csvRecord  o registro CSV que causou o error.
     * @param headerName o nome do cabeçalho relacionado ao error.
     */
    private static <T extends CSVImportable> void addErrorMessage(T object, String message, CSVRecord csvRecord, String headerName) {
        String formattedMessage = String.format("[Linha: %d - Header %s] %s",
                csvRecord.getRecordNumber(),
                headerName,
                message);

        object.addErroImportacao(formattedMessage);
    }

    /**
     * Método para converter uma string em um tipo específico.
     *
     * @param value a string a ser convertida.
     * @param type  o tipo para o qual a ‘string’ deve ser convertida.
     * @return o valor convertido para o tipo especificado.
     */
    private static Object convertStringToType(String value, Class<?> type) {
        if (type == String.class) {
            return value;
        } else if (type == Integer.class || type == int.class) {
            return Integer.parseInt(value);
        } else if (type == Double.class || type == double.class) {
            return Double.parseDouble(value);
        } else if (type == Boolean.class || type == boolean.class) {
            return Boolean.parseBoolean(value);
        } else if (type == Long.class || type == long.class) {
            return Long.parseLong(value);
        } else if (type == Float.class || type == float.class) {
            return Float.parseFloat(value);
        } else if (type == LocalDate.class) {
            return DateUtils.textToDate(value, LocalDate.class);
        } else if (type == LocalDateTime.class) {
            return DateUtils.textToDate(value, java.time.LocalDateTime.class);
        } else if (type == ZonedDateTime.class) {
            return DateUtils.textToDate(value, java.time.ZonedDateTime.class);
        } else if (type == Instant.class) {
            return DateUtils.textToDate(value, Instant.class);
        }
        throw new IllegalArgumentException("Tipo não suportado: " + type.getName());
    }

    /**
     * Método para validar os cabeçalhos do CSV em relação aos campos anotados com @PropriedadesCSV na classe ImportavelCSV.
     *
     * @param parser o CSVParser contendo os dados do CSV.
     * @param clazz  a classe que implementa ImportavelCSV.
     * @param <T>    o tipo da classe que implementa ImportavelCSV.
     */
    private static <T extends CSVImportable> void validateHeaders(CSVParser parser, Class<T> clazz) {
        List<String> headers = parser.getHeaderNames();
        List<String> permittedHeaders = getPermittedHeaders(clazz);
        List<String> requiredHeaders = getRequiredHeaders(clazz);

        if (headers.isEmpty()) {
            throw new IllegalArgumentException("O CSV não contém cabeçalhos.");
        }
        if (permittedHeaders.isEmpty()) {
            throw new IllegalArgumentException("A classe " + clazz.getName() + " não possui campos anotados com @PropriedadesCSV.");
        }
        for (String header : headers) {
            if (!permittedHeaders.contains(header)) {
                throw new IllegalArgumentException("Cabeçalho inválido: " + header + ". Cabeçalhos permitidos: " + permittedHeaders);
            }
            if (requiredHeaders.contains(header) && !headers.contains(header)) {
                throw new IllegalArgumentException("Cabeçalho obrigatório ausente: " + header + ". Cabeçalhos obrigatórios: " + requiredHeaders);
            }
        }
    }

    /**
     * Método para obter os cabeçalhos obrigatórios de uma classe que implementa ImportavelCSV.
     *
     * @param clazz a classe que implementa ImportavelCSV.
     * @param <T>   o tipo da classe que implementa ImportavelCSV.
     * @return uma lista de ‘strings’ contendo os cabeçalhos obrigatórios.
     */
    private static <T extends CSVImportable> List<String> getRequiredHeaders(Class<T> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("A classe não deve ser nula.");
        }

        List<Field> classFields = getClassFields(clazz);

        return classFields.stream()
                .filter(field -> field.isAnnotationPresent(CsvProperties.class))
                .filter(field -> field.getAnnotation(CsvProperties.class).required())
                .map(Field::getName).collect(Collectors.toList());
    }

    /**
     * Método para obter os campos permitidos de uma classe que implementa ImportavelCSV.
     *
     * @param clazz a classe que implementa ImportavelCSV.
     * @param <T>   o tipo da classe que implementa ImportavelCSV.
     * @return uma lista de ‘Field’ contendo os campos permitidos.
     */
    private static <T extends CSVImportable> List<Field> getClassFields(Class<T> clazz) {
        List<Field> permittedFields = new ArrayList<>(Arrays.asList(clazz.getDeclaredFields()));
        if (clazz.getSuperclass() != null) {
            permittedFields.addAll(Arrays.asList(clazz.getSuperclass().getDeclaredFields()));
        }
        return permittedFields;
    }

    /**
     * Método para obter os cabeçalhos permitidos de uma classe que implementa ImportavelCSV.
     *
     * @param clazz a classe que implementa ImportavelCSV.
     * @param <T>   o tipo da classe que implementa ImportavelCSV.
     * @return uma lista de ‘strings’ contendo os cabeçalhos permitidos.
     */
    private static <T extends CSVImportable> List<String> getPermittedHeaders(Class<T> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("A classe não deve ser nula.");
        }

        List<Field> permittedFields = getClassFields(clazz);
        List<String> headers = permittedFields.stream()
                .filter(field -> field.isAnnotationPresent(CsvProperties.class))
                .map(Field::getName).collect(Collectors.toList());

        if (headers.isEmpty()) {
            throw new IllegalArgumentException("A classe " + clazz.getName() + " não possui campos anotados com @PropriedadesCSV.");
        }

        return headers;
    }
}
