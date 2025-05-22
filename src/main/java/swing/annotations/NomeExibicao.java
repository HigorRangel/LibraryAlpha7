package swing.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NomeExibicao {

    /**
     * Nome do campo a ser exibido na tela
     * @return String
     */
    String value();
}
