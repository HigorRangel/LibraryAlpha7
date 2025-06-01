package swing.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação que define o nome a ser exibido na tela para um campo.
 * Utilizada para personalizar a exibição de campos em tabelas.
 * Exemplo de uso:
 * <pre>
 *     {@code
 *     @DisplayableName("Nome do Campo")
 *     private String campo;
 *     }
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DisplayableName {

    /**
     * Nome do campo a ser exibido na tela
     *
     * @return String
     */
    String value();
}
