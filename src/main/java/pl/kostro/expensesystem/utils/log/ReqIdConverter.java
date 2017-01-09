package pl.kostro.expensesystem.utils.log;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;

import com.vaadin.server.VaadinSession;

@Plugin(name = "ReqIdConverter", category = "Converter")
@ConverterKeys({ "reqId" })
public class ReqIdConverter extends LogEventPatternConverter {

  protected ReqIdConverter(String name, String style) {
    super(name, style);
  }

  public static ReqIdConverter newInstance(String[] options) {
    return new ReqIdConverter("reqId", "reqId");
  }

  @Override
  public void format(LogEvent event, StringBuilder toAppendTo) {
    toAppendTo.append(getReqId());
  }

  protected String getReqId() {
    return VaadinSession.getCurrent().getSession().getId();
}
}
