package swing.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação que indica que o campo não deve ser filtrado em casos de filtros
 * ou campos de buscas em tabelas, ou listas.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.FIELD})
public @interface NotFilterable {
}
