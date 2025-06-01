package swing.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.Temporal;
import java.util.*;

/**
 * Utilitário para manipulação de datas.
 */
public class DateUtils {

    /**
     * Lista de formatos aceitos para conversão de data
     */
    private static final List<String> permittedFormats = Arrays.asList(
            "dd/MM/yyyy HH:mm:ss",
            "dd/MM/yyyy HH:mm",
            "dd/MM/yyyy"
    );

    private DateUtils() {
    }

    /**
     * Converte uma data do tipo Date para ‘String’ (texto)
     *
     * @param date        Data a ser convertida
     * @param excludeTime Se deve excluir a hora da data
     * @return Data convertida para ‘String’
     */
    public static String dateToText(Temporal date, boolean excludeTime) {
        if (date == null) return null;

        if (date instanceof LocalDate) {
            LocalDate localDate = (LocalDate) date;
            return localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }

        if (date instanceof LocalDateTime) {
            LocalDateTime dateTime = (LocalDateTime) date;
            DateTimeFormatter formatter = excludeTime
                    ? DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    : DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            return dateTime.format(formatter);
        }

        if (date instanceof Instant) {
            Instant instant = (Instant) date;
            ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
            DateTimeFormatter formatter = excludeTime
                    ? DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    : DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            return zdt.format(formatter);
        }

        throw new IllegalArgumentException("Tipo de data não suportado: " + date.getClass().getSimpleName());
    }

    /**
     * Converte uma ‘string’ de text num objeto de data do tipo especificado.
     * <p>Suporta os seguintes formatos:</p>
     * <ul>
     *   <li>Estilo OpenLibrary: "April 15, 1997", "March 2009", "1985"</li>
     *   <li>Formato brasileiro: "dd/MM/yyyy" e "dd/MM/yyyy HH:mm:ss"</li>
     * </ul>
     *
     * @param text        Texto a ser convertido
     * @param targetClass Classe do tipo de data desejado
     * @param <T>         Tipo temporal de retorno (ex: {@code LocalDate}, {@code LocalDateTime}, {@code Instant})
     * @return Objeto convertido ou {@code null} se não for possível converter
     */
    public static <T extends Temporal> T textToDate(String text, Class<T> targetClass) {
        if (text == null || text.trim().isEmpty()) return null;

        String input = text.trim().replaceAll("[?c]", "");

        T result = tryOpenLibraryFormats(input, targetClass);
        if (result != null) return result;

        return tryBrazilianFormats(input, targetClass);
    }

    /**
     * Tenta converter o texto usando formatos baseados em publicações literárias em inglês.
     * <p>Exemplos de formatos válidos: "April 15, 1997", "March 2009", "1985".</p>
     *
     * @param input       Texto limpo a ser interpretado
     * @param targetClass Classe do tipo de data desejado
     * @param <T>         Tipo temporal de retorno
     * @return Objeto de data convertido, ou {@code null} se falhar
     */
    @SuppressWarnings("unchecked")
    private static <T extends Temporal> T tryOpenLibraryFormats(String input, Class<T> targetClass) {
        List<DateTimeFormatter> formatters = new ArrayList<>(
                Arrays.asList(
                        DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH),
                        DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)
                )
        );

        for (DateTimeFormatter formatter : formatters) {
            try {
                if (formatter.toString().contains("d,")) {
                    if (targetClass.equals(LocalDate.class)) {
                        return (T) LocalDate.parse(input, formatter);
                    }
                } else {
                    if (targetClass.equals(LocalDate.class)) {
                        YearMonth ym = YearMonth.parse(input, formatter);
                        return (T) ym.atDay(1);
                    }
                }
            } catch (DateTimeParseException ignored) {
            }
        }

        try {
            int ano = Integer.parseInt(input);
            if (targetClass.equals(LocalDate.class)) {
                return (T) LocalDate.of(ano, 1, 1);
            }
        } catch (NumberFormatException ignored) {
        }

        return null;
    }

    /**
     * Tenta converter o texto usando formatos de data no estilo brasileiro.
     * <p>Exemplos de formatos válidos: "dd/MM/yyyy", "dd/MM/yyyy HH:mm:ss".</p>
     *
     * @param input       Texto limpo a ser interpretado
     * @param targetClass Classe do tipo de data desejado
     * @param <T>         Tipo temporal de retorno
     * @return Objeto de data convertido, ou {@code null} se falhar
     */
    @SuppressWarnings("unchecked")
    private static <T extends Temporal> T tryBrazilianFormats(String input, Class<T> targetClass) {
        List<String> formats = new ArrayList<>(permittedFormats);

        for (String pattern : formats) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                Date data = sdf.parse(input);

                if (targetClass.equals(LocalDate.class)) {
                    return (T) data.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                } else if (targetClass.equals(LocalDateTime.class)) {
                    return (T) data.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                } else if (targetClass.equals(Instant.class)) {
                    return (T) data.toInstant();
                }
            } catch (ParseException ignored) {
            }
        }
        return null;
    }


    /**
     * Verifica se a data é anterior a hoje.
     *
     * @param temporal Data a ser verificada
     * @return true se a data for anterior a hoje, false caso contrário
     */
    public static boolean isDateAfterToday(Temporal temporal) {
        if (temporal == null) {
            return false;
        }
        LocalDate hoje = LocalDate.now();
        if (temporal instanceof LocalDate) {
            return ((LocalDate) temporal).isAfter(hoje);
        } else if (temporal instanceof LocalDateTime) {
            return ((LocalDateTime) temporal).toLocalDate().isAfter(hoje);
        } else if (temporal instanceof Instant) {
            ZonedDateTime zdt = ((Instant) temporal).atZone(ZoneId.systemDefault());
            return zdt.toLocalDate().isAfter(hoje);
        }
        return false;
    }
}
