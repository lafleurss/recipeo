package recipeo.converters;

import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LocalDateTimeConverterTest {
   LocalDateTimeConverter converter = new LocalDateTimeConverter();

    @Test
    void convert() {
        // GIVEN
        LocalDateTime givenDateTime = LocalDateTime.parse("2020-12-03T12:20:59.000");

        // WHEN
        String convertedTime = converter.convert(givenDateTime);

        // THEN
        assertEquals("2020-12-03T12:20:59.000", convertedTime);
    }

    @Test
    void unconvert() {
        // GIVEN
        String time = "2020-12-03T12:20:59.000";
        LocalDateTime expected = LocalDateTime.parse("2020-12-03T12:20:59.000");

        // WHEN
        LocalDateTime unconvertedTime = LocalDateTime.from(converter.unconvert(time));

        // THEN
        assertEquals(expected, unconvertedTime);
    }
}
