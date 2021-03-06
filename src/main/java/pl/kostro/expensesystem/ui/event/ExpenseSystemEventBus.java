package pl.kostro.expensesystem.ui.event;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;

import pl.kostro.expensesystem.ui.ExpenseSystemUI;

public class ExpenseSystemEventBus implements SubscriberExceptionHandler {

    private final EventBus eventBus = new EventBus(this);

    public static void post(final Object event) {
        ExpenseSystemUI.getExpenseEventbus().eventBus.post(event);
    }

    public static void register(final Object object) {
      ExpenseSystemUI.getExpenseEventbus().eventBus.register(object);
    }

    public static void unregister(final Object object) {
      ExpenseSystemUI.getExpenseEventbus().eventBus.unregister(object);
    }

    @Override
    public final void handleException(final Throwable exception,
            final SubscriberExceptionContext context) {
        exception.printStackTrace();
    }
}
