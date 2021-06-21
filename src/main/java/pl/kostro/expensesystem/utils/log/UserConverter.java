package pl.kostro.expensesystem.utils.log;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;

import com.vaadin.server.VaadinSession;

import pl.kostro.expensesystem.model.RealUserEntity;

@Plugin(name = "UserConverter", category = "Converter")
@ConverterKeys({ "user" })
public class UserConverter extends LogEventPatternConverter {

  protected UserConverter(String name, String style) {
    super(name, style);
  }

  public static UserConverter newInstance(String[] options) {
    return new UserConverter("user", "user");
  }

  @Override
  public void format(LogEvent event, StringBuilder toAppendTo) {
    toAppendTo.append(getUser());
  }

  protected String getUser() {
    if (VaadinSession.getCurrent() != null) {
      RealUserEntity user = VaadinSession.getCurrent().getAttribute(RealUserEntity.class);
      if (user != null)
        return user.getName();
      else
        return "anonymus";
    } else {
      return "none";
    }
  }
}
