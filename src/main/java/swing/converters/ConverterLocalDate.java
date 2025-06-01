package swing.converters;

import swing.interfaces.CSVConverter;
import swing.util.DateUtils;

import java.time.LocalDate;

/**
 * Conversor para converter uma ‘string’ num objeto LocalDate.
 */
public class ConverterLocalDate implements CSVConverter<LocalDate> {

    /**
     * Converte uma ‘string’ representando uma data num objeto LocalDate.
     *
     * @param valor a string contendo a data a ser convertida, no formato esperado (por exemplo, "dd/MM/yyyy").
     * @return um objeto LocalDate correspondente à data fornecida.
     */
    @Override
    public LocalDate converter(String valor) {
        return DateUtils.textToDate(valor, LocalDate.class);
    }
}
