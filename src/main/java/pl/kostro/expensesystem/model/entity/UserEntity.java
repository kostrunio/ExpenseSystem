package pl.kostro.expensesystem.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import pl.kostro.expensesystem.model.converter.LocalDateTimePersistenceConverter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "u_type", discriminatorType = DiscriminatorType.INTEGER)
@DiscriminatorValue(value = "1")
public class UserEntity extends AbstractEntity {
  @Id
  @GeneratedValue(generator="increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  @Column(name = "u_id")
  private Long id;
  @Column(name = "u_name")
  private String name;
  @Column(name = "u_creation_date")
  @Convert(converter = LocalDateTimePersistenceConverter.class)
  private LocalDateTime creationDate = LocalDateTime.now();

  public UserEntity() {
    super();
  }

  public UserEntity(String name) {
    setName(name);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName()+"["+getName()+"]";
  }

}
