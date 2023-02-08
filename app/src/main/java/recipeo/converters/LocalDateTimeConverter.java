package recipeo.converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeConverter implements DynamoDBTypeConverter<String, LocalDateTime> {

    /**
     * @param object to be converted to String
     * @return String value of the
     */
    @Override
    public String convert(LocalDateTime object) {
        if (object == null) {
            return null;
        }
        return object.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS"));
    }

    /**
     * @param object to be converted to ZoneDateTime
     * @return ZonedDateTime value of object
     */
    @Override
    public LocalDateTime unconvert(String object) {
        if (object == null) {
            return null;
        }
        return LocalDateTime.parse(object, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS"));
    }
}
