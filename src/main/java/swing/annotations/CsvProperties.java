package swing.annotations;

import swing.converters.DefaultConverter;
import swing.interfaces.CSVConverter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação que define as propriedades de um campo para exportação em CSV.
 * Utilizada para definir se o campo é obrigatório e qual conversor CSV deve ser utilizado.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface CsvProperties {

    /**
     * Define se é um campo obrigatório ou não
     *
     * @return se o campo é obrigatório, padrão é false
     */
    boolean required() default false;

    /**
     * Classe do csvConverter a ser utilizado para converter o campo
     *
     * @return Class<? extends ConversorCSV < ?>>
     */
    Class<? extends CSVConverter<?>> csvConverter() default DefaultConverter.class;


}
