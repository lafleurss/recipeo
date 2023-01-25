package recipeo.converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ZonedDateTimeConverter implements DynamoDBTypeConverter<String, ZonedDateTime> {

    /**
     * @param object to be converted to String
     * @return String value of the
     */
    @Override
    public String convert(ZonedDateTime object) {
        if (object == null){
            return null;
        }
        return object.format(DateTimeFormatter.ISO_DATE_TIME);
    }

    /**
     * @param object to be converted to ZoneDateTime
     * @return
     */
    @Override
    public ZonedDateTime unconvert(String object) {
        if (object == null){
            return null;
        }
        return ZonedDateTime.parse(object,  DateTimeFormatter.ISO_DATE_TIME);
    }
}
