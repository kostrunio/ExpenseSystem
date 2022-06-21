package pl.kostro.expensesystem.newui.dialog;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import pl.kostro.expensesystem.newui.dialog.ConfirmDialog.Factory;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * This is the default implementation for confirmation dialog factory.
 *
 * This supports text only content and tries to approximate the the dialog size.
 *
 * TODO: Allow configuration of min and max sizes.
 *
 * @author Sami Ekblad
 *
 */
public class DefaultConfirmDialogFactory implements Factory {

    // System wide defaults
    protected static final String DEFAULT_CAPTION = "Confirm";
    protected static final String DEFAULT_MESSAGE = "Are You sure?";
    protected static final String DEFAULT_OK_CAPTION = "Ok";
    protected static final String DEFAULT_CANCEL_CAPTION = "Cancel";

    // System wide defaults
    private static final double MIN_WIDTH = 20d;
    private static final double MAX_WIDTH = 40d;
    private static final double MIN_HEIGHT = 1d;
    private static final double MAX_HEIGHT = 30d;
//    private static final double BUTTON_HEIGHT = 2.5;

    public ConfirmDialog create(final String caption, final String message,
                                final String okCaption, final String cancelCaption,
                                final String notOkCaption) {

        final boolean threeWay = notOkCaption != null;
        // Create a confirm dialog
        final ConfirmDialog confirm = new ConfirmDialog();
        confirm.setId(ConfirmDialog.DIALOG_ID);
        confirm.setHeaderTitle(caption != null ? caption : DEFAULT_CAPTION);

        // Close listener implementation
        /*confirm.addCloseListener(new Window.CloseListener() {
            public void windowClose(CloseEvent ce) {
                if (confirm.isEnabled()) {
                    confirm.setEnabled(false); // avoid double processing
                    confirm.setConfirmed(false);
                    if (confirm.getListener() != null) {
                        confirm.getListener().onClose(confirm);
                    }
                }
            }
        });*/

        // Create content
        VerticalLayout c = new VerticalLayout();
        confirm.add(c);
        c.setSizeFull();

        // Panel for scrolling lengthy messages.
        VerticalLayout scrollContent = new VerticalLayout();
        Div panel = new Div(scrollContent);
        c.add(panel);
        panel.setWidth("100%");
        panel.setHeight("100%");
        panel.addClassName(ValoTheme.PANEL_BORDERLESS); // valo compatibility
        c.setFlexGrow(1f, panel);

 /*       // Always HTML, but escape
        Label text = new Label(""*//*, com.vaadin.shared.ui.ContentMode.HTML*//*);
        text.setId(ConfirmDialog.MESSAGE_ID);
        scrollContent.add(text);
        confirm.setMessageLabel(text);*/
        confirm.setMessage(message);

        HorizontalLayout buttons = new HorizontalLayout();
        c.add(buttons);
        c.setAlignItems(FlexComponent.Alignment.START);
        buttons.setMargin(false);

        final Button ok = new Button(okCaption != null ? okCaption : DEFAULT_OK_CAPTION);
//        ok.setData(true);
        ok.setId(ConfirmDialog.OK_ID);
        ok.addClickShortcut(Key.ENTER);
        ok.setClassName(ValoTheme.BUTTON_DANGER);
        ok.focus();
        buttons.add(ok);
        confirm.setOkButton(ok);
        
        Button notOk = null;
        if (threeWay) {
            notOk = new Button(notOkCaption);
//            notOk.setData(false);
            notOk.setId(ConfirmDialog.NOT_OK_ID);
            buttons.add(notOk);
            confirm.setCancelButton(notOk);
        }
        
        final Button cancel = new Button(cancelCaption != null ? cancelCaption
                : DEFAULT_CANCEL_CAPTION);
//        cancel.setData(null);
        cancel.setId(ConfirmDialog.CANCEL_ID);
        cancel.addClickShortcut(Key.ESCAPE);
        buttons.add(cancel);
        confirm.setCancelButton(cancel);

        // Create a listener for buttons
        ComponentEventListener<ClickEvent<Button>> cb = event -> {
            // Copy the button date to window for passing through either
            // "OK" or "CANCEL". Only process id still enabled.
            if (confirm.isEnabled()) {
                confirm.setEnabled(false); // Avoid double processing

                Button b = event.getSource();
                if (b != cancel)
                    confirm.setConfirmed(b == ok);

                // We need to cast this way, because of the backward
                // compatibility issue in 6.4 series.
//                UI parent = confirm.getUI();
//                parent.removeWindow(confirm);

                // This has to be invoked as the window.close
                // event is not fired when removed.
                if (confirm.getListener() != null) {
                    confirm.getListener().onClose(confirm);
                }
            }
        };

        cancel.addClickListener(cb);
        ok.addClickListener(cb);
        if (notOk != null)
            notOk.addClickListener(cb);

        // Approximate the size of the dialog
        double[] dim = getDialogDimensions(message,
                ConfirmDialog.ContentMode.TEXT_WITH_NEWLINES);
        confirm.setWidth(format(dim[0]) + "em");
        confirm.setHeight(format(dim[1]) + "em");
        confirm.setResizable(false);

        return confirm;
    }

    /**
     * Approximates the dialog dimensions based on its message length.
     *
     * @param message
     *            Message string
     * @return approximate size for the dialog with given message
     */
    protected double[] getDialogDimensions(String message,
            ConfirmDialog.ContentMode style) {

        // Based on Reindeer style:
        double chrW = 0.51d;
        double chrH = 1.5d;
        double length = message != null? chrW * message.length() : 0;
        double rows = Math.ceil(length / MAX_WIDTH);

        // Estimate extra lines
        if (style == ConfirmDialog.ContentMode.TEXT_WITH_NEWLINES) {
            rows += message != null? count("\n", message): 0;
        }

        //System.out.println(message.length() + " = " + length + "em");
        //System.out.println("Rows: " + (length / MAX_WIDTH) + " = " + rows);

        // Obey maximum size
        double width = Math.min(MAX_WIDTH, length);
        double height = Math.ceil(Math.min(MAX_HEIGHT, rows * chrH));

        // Obey the minimum size
        width = Math.max(width, MIN_WIDTH);
        height = Math.max(height, MIN_HEIGHT);

        // Based on Reindeer style:
        double btnHeight = 4d;
        double vmargin = 5d;
        double hmargin = 1d;

        double[] res = new double[] { width + hmargin,
                height + btnHeight + vmargin };
        //System.out.println(res[0] + "," + res[1]);
        return res;
    }

    /**
     * Count the number of needles within a haystack.
     *
     * @param needle
     *            The string to search for.
     * @param haystack
     *            The string to process.
     * @return count of needles within a haystack
     */
    private static int count(final String needle, final String haystack) {
        int count = 0;
        int pos = -1;
        while ((pos = haystack.indexOf(needle, pos + 1)) >= 0) {
            count++;
        }
        return count;
    }

    /**
     * Format a double single fraction digit.
     *
     * @param n
     * @return
     */
    private String format(double n) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        nf.setMaximumFractionDigits(1);
        nf.setGroupingUsed(false);
        return nf.format(n);
    }

}
