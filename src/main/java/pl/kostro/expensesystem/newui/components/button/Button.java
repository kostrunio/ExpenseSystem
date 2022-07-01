package pl.kostro.expensesystem.newui.components.button;

public class Button extends com.vaadin.flow.component.button.Button {
    private Object data;

    public void setData(Object data) {
        this.data = data;
    }
    public Object getData() {
        return data;
    }
}
