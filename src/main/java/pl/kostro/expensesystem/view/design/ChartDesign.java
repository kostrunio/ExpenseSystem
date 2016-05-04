package pl.kostro.expensesystem.view.design;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.declarative.Design;

/** 
 * !! DO NOT EDIT THIS FILE !!
 * 
 * This class is generated by Vaadin Designer and will be overwritten.
 * 
 * Please make a subclass with logic and additional interfaces as needed,
 * e.g class LoginView extends LoginDesign implements View { … }
 */
@DesignRoot
@AutoGenerated
@SuppressWarnings("serial")
public class ChartDesign extends HorizontalLayout {
  protected VerticalLayout chartLayout;
  protected Panel searchPanel;
  protected VerticalLayout searchLayout;
  protected OptionGroup categoryCombo;
  protected OptionGroup userCombo;
  protected Button searchButton;

  public ChartDesign() {
    Design.read(this);
  }
}