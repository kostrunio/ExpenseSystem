package pl.kostro.expensesystem.model.converter;
import java.time.LocalDateTime;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class LocalDateTimePersistenceConverter implements AttributeConverter<LocalDateTime, java.sql.Timestamp> {

  @Override
  public java.sql.Timestamp convertToDatabaseColumn(LocalDateTime entityValue) {
    if (entityValue != null) {
      return java.sql.Timestamp.valueOf(entityValue);
    }
    return null;
  }

  @Override
  public LocalDateTime convertToEntityAttribute(java.sql.Timestamp databaseValue) {
    if (databaseValue != null) {
      return databaseValue.toLocalDateTime();
    }
    return null;
  }
}
