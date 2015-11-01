package pl.kostro.expensesystem.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import pl.kostro.expensesystem.utils.Encryption;

import com.vaadin.server.VaadinSession;

@Entity
@Table(name="users")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="u_type", discriminatorType=DiscriminatorType.INTEGER)
@DiscriminatorValue(value="1")
public class User extends AbstractEntity {
  
  private static final long serialVersionUID = -2614536668038640488L;

  @Id
  @GeneratedValue(generator="increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  @Column(name="u_id")
  private int id;
  @Column(name="u_name")
  private String name;
  @Column(name = "es_name_byte")
  private byte[] name_byte;
  @Column(name="u_creation_date")
  private Date creationDate = new Date();
  
  public User() {
    super();
  }

  public User(String name) {
    setName(name);
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    if (name_byte != null) {
      Encryption enc = new Encryption(VaadinSession.getCurrent().getAttribute(RealUser.class).getKeyString());
      name = enc.decryption(name_byte);
    }
    return name;
  }

  public void setName(String name) {
    Encryption enc = new Encryption(VaadinSession.getCurrent().getAttribute(RealUser.class).getKeyString());
    this.name_byte = enc.encryption(name);
    this.name = name;
  }
  
  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  @Override
  public String toString() {
    return getName();
  }
  
  @Override
  public boolean equals(Object o) {
    if(o instanceof User)
      return getId() == ((User)o).getId();
    else return this == o;
  }
  
  @Override
  public int hashCode() {
	  int hash = id;
	  hash += name.hashCode();
	  hash += creationDate.hashCode();
	  return hash;
  }
}
