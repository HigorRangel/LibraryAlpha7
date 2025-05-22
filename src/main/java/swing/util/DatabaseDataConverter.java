package swing.util;

import swing.enums.Status;

import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.Timestamp;

public class DatabaseDataConverter
{
    public static Object defaultConversions(Field field, Object value) {
//        return switch (value) {
//            case null -> null;
//            case Timestamp timestamp -> timestamp.toInstant();
//            case java.util.Date date -> date.toInstant();
//            default -> value;
//        };

        if (value instanceof Timestamp) {
            return ((Timestamp) value).toInstant();
        } else if (value instanceof java.util.Date) {
            return ((Date) value).toInstant();
        } else if (value instanceof String && field.getType().equals(Status.class)) {
            return Status.fromCharacter((String) value);
        } else {
            return value;
        }
    }
}
