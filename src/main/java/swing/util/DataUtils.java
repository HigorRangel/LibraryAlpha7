package swing.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class DataUtils {

    /**
     * Lista de formatos aceitos para conversão de data
     */
    private static final List<String> formatosAceitos = Arrays.asList(
            "dd/MM/yyyy HH:mm:ss",
            "dd/MM/yyyy HH:mm",
            "dd/MM/yyyy"
    );

    /**
     * Converte uma data do tipo Date para ‘String’ (texto)
     * @param date Data a ser convertida
     * @param excludeTime Se deve excluir a hora da data
     * @return Data convertida para ‘String’
     */
    public static String formatarDataParaString(Temporal date, boolean excludeTime) {
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
     *  Converte uma data do tipo ‘String’ (texto) para Date
     * @param data Data a ser convertida
     * @param targetClass Classe alvo para conversão
     * @return Data convertida
     * @param <T> Tipo de data
     */
    @SuppressWarnings("unchecked")
    public static <T extends Temporal> T formatarTextoParaData(String data, Class<T> targetClass){
        if(data == null || data.isEmpty()){
            return null;
        }
        if(isDataValida(data)){
            return null;
        }
        boolean containsTime = data.contains(":");
        String format = null;
        if(containsTime){
            format = "dd/MM/yyyy HH:mm:ss";
        } else {
            format = "dd/MM/yyyy";
        }

        SimpleDateFormat formatter = new SimpleDateFormat(format);
        try {
            if(targetClass.equals(LocalDate.class)){
                Date date = formatter.parse(data);
                return (T) date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            } else if(targetClass.equals(java.time.LocalDateTime.class)){
                Date date = formatter.parse(data);
                return (T) date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
            } else if(targetClass.equals(java.time.Instant.class)){
                Date date = formatter.parse(data);
                return (T) date.toInstant();
            } else {
                throw new IllegalArgumentException("Tipo de data não suportado: " + targetClass.getName());
            }
        } catch (ParseException e) {
            return null;
        }
    }


    /**
     * Tenta fazer o parse de uma data num formato específico.
     * @param dataStr a ‘string’ da data a ser validada
     * @param formato o formato a ser validado
     * @return true se for uma data válida, false caso contrário
     */
    private static boolean tentarParse(String dataStr, String formato) {
        SimpleDateFormat sdf = new SimpleDateFormat(formato);
        sdf.setLenient(false);
        try {
            sdf.parse(dataStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * Verifica se a ‘string’ é uma data válida em um dos formatos aceitos.
     *
     * @param dataStr a ‘string’ da data a ser validada
     * @return true se for uma data válida, false caso contrário
     */
    public static boolean isDataValida(String dataStr) {
        for (String formato : formatosAceitos) {
            if (tentarParse(dataStr, formato)) {
                return true;
            }
        }
        return false;
    }
}
