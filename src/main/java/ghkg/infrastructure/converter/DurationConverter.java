package ghkg.infrastructure.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.Duration;

@Converter(autoApply = true)
public class DurationConverter implements AttributeConverter<Duration, String> {

    @Override
    public String convertToDatabaseColumn(Duration duration) {
        return duration != null ? duration.toString() : null;
    }

    @Override
    public Duration convertToEntityAttribute(String value) {
        return value != null ? Duration.parse(value) : null;
    }
}
