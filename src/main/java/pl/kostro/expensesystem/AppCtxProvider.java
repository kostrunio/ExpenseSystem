package pl.kostro.expensesystem;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class AppCtxProvider implements ApplicationContextAware {

    private static ApplicationContext context;

    private AppCtxProvider(){}

    public static ApplicationContext getApplicationContext() {
        return context;
    }

    public  static <T> T getBean(Class<T> aClass){
        return context.getBean(aClass);
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        context = ctx;
    }
}