package pl.kostro.expensesystem.view.design;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import pl.kostro.expensesystem.view.AddNewExpenseView;

@SuppressWarnings("serial")
public class CategoryExpenseDesign extends VerticalLayout {

  protected Label categoryLabel;
  protected HorizontalLayout expenseLayout;
  protected GridLayout expenseGrid;
  protected AddNewExpenseView addNewExpense;

  public CategoryExpenseDesign() {
    setSizeFull();

    // categoryLabel
    categoryLabel = new Label();
    addComponent(categoryLabel);
    setComponentAlignment(categoryLabel, new Alignment(20));

    // expenseLayout
    expenseLayout = buildExpenseLayout();
    addComponent(expenseLayout);
    setComponentAlignment(expenseLayout, new Alignment(48));

    addNewExpense = new AddNewExpenseView();
    addComponent(addNewExpense);
  }

  private HorizontalLayout buildExpenseLayout() {
    expenseLayout = new HorizontalLayout();

    expenseGrid = new GridLayout();
    expenseGrid.setSpacing(true);
    expenseLayout.addComponent(expenseGrid);

    return expenseLayout;
  }
}
