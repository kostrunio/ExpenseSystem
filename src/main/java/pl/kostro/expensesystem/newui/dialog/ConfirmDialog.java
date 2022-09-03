package pl.kostro.expensesystem.newui.dialog;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.server.JsonPaintTarget;

import java.io.Serializable;

public class ConfirmDialog extends Dialog {

  public interface Factory extends Serializable {
        ConfirmDialog create(String windowCaption, String message,
                String okTitle, String cancelTitle);
    }

    /* Test IDs for elements */
    public static final String DIALOG_ID = "confirmdialog-window";
    public static final String MESSAGE_ID = "confirmdialog-message";
    public static final String OK_ID = "confirmdialog-ok-button";
    public static final String NOT_OK_ID = "confirmdialog-not-ok-button";
    public static final String CANCEL_ID = "confirmdialog-cancel-button";

    public enum ContentMode {
        TEXT_WITH_NEWLINES, TEXT, PREFORMATTED, HTML
    };

    /**
     * Listener for dialog close events. Implement and register an instance of
     * this interface to dialog to receive close events.
     *
     * @author Sami Ekblad
     *
     */
    public interface Listener extends Serializable {
        void onClose(ConfirmDialog dialog);
    }

    /**
     * Default dialog factory.
     *
     */
    private static Factory factoryInstance;

    /**
     * Get the ConfirmDialog.Factory used to create and configure the dialog.
     *
     * By default the {@link DefaultConfirmDialogFactory} is used.
     *
     * @return the currently used ConfirmDialog.Factory
     */
    public static Factory getFactory() {
        if (factoryInstance == null) {
            factoryInstance = new DefaultConfirmDialogFactory();
        }
        return factoryInstance;
    }

    /**
     * Set the ConfirmDialog.Factory used to create and configure the dialog.
     *
     * By default the {@link DefaultConfirmDialogFactory} is used.
     * 
     * @param newFactory the ConfirmDialog factory to be used
     *
     */
    public static void setFactory(final Factory newFactory) {
        factoryInstance = newFactory;
    }

    /**
     * Show a modal ConfirmDialog in a window.
     *
//     * @param ui
//     *            Main level UI.
     * @param windowCaption
     *            Caption for the confirmation dialog window.
     * @param message
     *            Message to display as window content.
     * @param okCaption
     *            Caption for the ok button.
     * @param cancelCaption
     *            Caption for cancel button.
     * @param listener
     *            Listener for dialog result.
     * @return the ConfirmDialog that was instantiated
     */
    public static ConfirmDialog show(final String windowCaption, final String message,
            final String okCaption, final String cancelCaption,
            final Listener listener) {
        ConfirmDialog d = getFactory().create(windowCaption, message,
                okCaption, cancelCaption);
        d.show(listener, true);
        return d;
    }

    private Listener confirmListener = null;
    private Boolean isConfirmed = null;
    private Label messageLabel = null;
    private Button okBtn = null;
    private Button cancelBtn = null;
    private String originalMessageText;
    private ContentMode msgContentMode = ContentMode.TEXT_WITH_NEWLINES;

    /**
     * Show confirm dialog.
     *
//     * @param ui the UI in which the dialog should be shown
     * @param listener the listener to be notified
     * @param modal true if the dialog should be modal
     */
    public final void show(final Listener listener,
            final boolean modal) {
        confirmListener = listener;
//        center();
        setModal(modal);
//        ui.addWindow(this);
    }

    /**
     * Did the user confirm the dialog.
     *
     * @return true if user confirmed
     */
    public final boolean isConfirmed() {
        return isConfirmed != null && isConfirmed;
    }

    /**
     * Did the user cancel the dialog.
     * 
     * @return true if the dialog was canceled
     */
    public final boolean isCanceled() {
        return isConfirmed == null;
    }

    public final Listener getListener() {
        return confirmListener;
    }

    protected final void setOkButton(final Button okButton) {
        okBtn = okButton;
    }

    public final Button getOkButton() {
        return okBtn;
    }

    protected final void setCancelButton(final Button cancelButton) {
        cancelBtn = cancelButton;
    }

    public final Button getCancelButton() {
        return cancelBtn;
    }

    protected final void setMessageLabel(final Label message) {
        messageLabel = message;
    }

    public final void setMessage(final String message) {
        originalMessageText = message;
        messageLabel
                .setText(ContentMode.TEXT_WITH_NEWLINES == msgContentMode ? formatDialogMessage(message)
                        : message);
    }

    public final String getMessage() {
        return originalMessageText;
    }

/*    public final ContentMode getContentMode() {
        return msgContentMode;
    }*/

/*    public final void setContentMode(final ContentMode contentMode) {
        msgContentMode = contentMode;
        com.vaadin.shared.ui.ContentMode labelContentMode = com.vaadin.shared.ui.ContentMode.TEXT;
        switch (contentMode) {
        case TEXT_WITH_NEWLINES:
        case TEXT:
            labelContentMode = com.vaadin.shared.ui.ContentMode.TEXT;
            break;
        case PREFORMATTED:
            labelContentMode = com.vaadin.shared.ui.ContentMode.PREFORMATTED;
            break;
        case HTML:
            labelContentMode = com.vaadin.shared.ui.ContentMode.HTML;
            break;
        }
        messageLabel
                .setContentMode(labelContentMode);
        messageLabel
                .setText(contentMode == ContentMode.TEXT_WITH_NEWLINES ? formatDialogMessage(originalMessageText)
                        : originalMessageText);
    }*/

    /**
     * Format the messageLabel by maintaining text only.
     *
     * @param text the text to be formatted
     * @return formatted text
     */
    protected final String formatDialogMessage(final String text) {
        return JsonPaintTarget.escapeXML(text).replaceAll("\n", "<br />");
    }

    /**
     * Set the isConfirmed state.
     *
     * Note: this should only be called internally by the listeners.
     *
     * @param confirmed true if dialog was confirmed
     */
    protected final void setConfirmed(final boolean confirmed) {
        isConfirmed = confirmed;
    }
}
