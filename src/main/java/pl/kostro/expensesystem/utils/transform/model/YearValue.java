package pl.kostro.expensesystem.utils.transform.model;

import java.math.BigDecimal;
import java.util.List;

public class YearValue {
  private int year;
  private BigDecimal january;
  private BigDecimal february;
  private BigDecimal march;
  private BigDecimal april;
  private BigDecimal may;
  private BigDecimal june;
  private BigDecimal july;
  private BigDecimal august;
  private BigDecimal september;
  private BigDecimal october;
  private BigDecimal november;
  private BigDecimal december;
  
  public YearValue(int year, List<BigDecimal> values) {
    super();
    this.year = year;
    this.january = values.get(0);
    this.february = values.get(1);
    this.march = values.get(2);
    this.april = values.get(3);
    this.may = values.get(4);
    this.june = values.get(5);
    this.july = values.get(6);
    this.august = values.get(7);
    this.september = values.get(8);
    this.october = values.get(9);
    this.november = values.get(10);
    this.december = values.get(11);
  }

  public int getYear() {
    return year;
  }

  public BigDecimal getJanuary() {
    return january;
  }

  public BigDecimal getFebruary() {
    return february;
  }

  public BigDecimal getMarch() {
    return march;
  }

  public BigDecimal getApril() {
    return april;
  }

  public BigDecimal getMay() {
    return may;
  }

  public BigDecimal getJune() {
    return june;
  }

  public BigDecimal getJuly() {
    return july;
  }

  public BigDecimal getAugust() {
    return august;
  }

  public BigDecimal getSeptember() {
    return september;
  }

  public BigDecimal getOctober() {
    return october;
  }

  public BigDecimal getNovember() {
    return november;
  }

  public BigDecimal getDecember() {
    return december;
  }
}
