package pl.kostro.expensesystem.utils.transform.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kostro.expensesystem.model.entity.CategoryEntity;
import pl.kostro.expensesystem.model.entity.ExpenseEntity;
import pl.kostro.expensesystem.model.entity.ExpenseSheetEntity;
import pl.kostro.expensesystem.model.entity.RealUserEntity;
import pl.kostro.expensesystem.model.entity.UserEntity;
import pl.kostro.expensesystem.model.entity.UserLimitEntity;
import pl.kostro.expensesystem.model.entity.UserSummaryEntity;
import pl.kostro.expensesystem.model.service.UserLimitService;
import pl.kostro.expensesystem.model.service.UserSummaryService;
import pl.kostro.expensesystem.utils.filter.Filter;
import pl.kostro.expensesystem.utils.transform.model.CategoryExpense;
import pl.kostro.expensesystem.utils.transform.model.DateExpense;
import pl.kostro.expensesystem.utils.transform.model.UserLimitExpense;
import pl.kostro.expensesystem.utils.transform.model.UserSumChange;
import pl.kostro.expensesystem.utils.transform.model.YearCategory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ExpenseSheetTransformServiceImpl implements ExpenseSheetTransformService {

  private final UserLimitService uls;
  private final UserSummaryService uss;

  @Autowired
  public ExpenseSheetTransformServiceImpl(UserLimitService uls, UserSummaryService uss) {
    this.uls = uls;
    this.uss = uss;
  }

  public Map<RealUserEntity, Map<ExpenseSheetEntity, List<ExpenseEntity>>> prepareExpenseSheetNotify(List<ExpenseEntity> expenseList) {
    Map<RealUserEntity, Map<ExpenseSheetEntity, List<ExpenseEntity>>> rUMap = new HashMap<>();
    for (ExpenseEntity expense : expenseList) {
      rUMap.getOrDefault(expense.getExpenseSheet().getOwner(), new HashMap<>()).getOrDefault(expense.getExpenseSheet(), new ArrayList<>()).add(expense);
    }
    return rUMap;
  }

  public ExpenseSheetEntity findExpenseSheet(RealUserEntity realUser, int id) {
    Optional<ExpenseSheetEntity> result = realUser.getExpenseSheetList().parallelStream()
            .filter(esh -> esh.getId() == id)
            .findFirst();
    return result.orElse(realUser.getDefaultExpenseSheet());
  }

  public List<ExpenseEntity> findAllExpenses(ExpenseSheetEntity expenseSheet) {
    return expenseSheet.getExpenseList().parallelStream()
            .filter(e -> Filter.matchFilter(e, expenseSheet.getFilter()))
            .collect(Collectors.toList());
  }

  private List<ExpenseEntity> getExpenseList(ExpenseSheetEntity expenseSheet) {
    return expenseSheet.getExpenseList().parallelStream()
            .filter(e -> !e.getDate().isBefore(expenseSheet.getFirstDate()))
            .filter(e -> !e.getDate().isAfter(expenseSheet.getLastDate()))
            .filter(e -> Filter.matchFilter(e, expenseSheet.getFilter()))
            .collect(Collectors.toList());
  }

  public void prepareExpenseMap(ExpenseSheetEntity expenseSheet, LocalDate startDate, LocalDate endDate,
                                                       LocalDate firstDay, LocalDate lastDay) {
    LocalDateTime stopper = LocalDateTime.now();
    clearMaps(expenseSheet);
    expenseSheet.setFirstDate(startDate);
    expenseSheet.setLastDate(endDate);
    for (ExpenseEntity expense : getExpenseList(expenseSheet)) {
      if (expense.getValue() == null) continue;
      addExpenseToDateMap(expense, expenseSheet);
      if (firstDay != null && lastDay != null && !expense.getDate().isBefore(firstDay)
              && !expense.getDate().isAfter(lastDay)) {
        addExpenseToCategoryMap(expense, expenseSheet);
        addExpenseToUserLimitMap(expense, expenseSheet);
      }
    }
    log.info("prepareExpenseMap for {} finish: {} ms", expenseSheet, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  private void clearMaps(ExpenseSheetEntity expenseSheet) {
    expenseSheet.getDateExpenseMap().clear();
    expenseSheet.getCategoryExpenseMap().clear();
    expenseSheet.getUserLimitExpenseMap().clear();
  }

  public void addExpense(ExpenseEntity expense, ExpenseSheetEntity expenseSheet) {
    expenseSheet.getExpenseList().add(expense);
    addExpenseToDateMap(expense, expenseSheet);
  }

  private void addExpenseToDateMap(ExpenseEntity expense, ExpenseSheetEntity expenseSheet) {
    DateExpense dateExpense = expenseSheet.getDateExpenseMap().get(expense.getDate());
    if (dateExpense == null) {
      dateExpense = new DateExpense(expense.getDate());
      expenseSheet.getDateExpenseMap().put(expense.getDate(), dateExpense);
    }
    dateExpense.addExpense(expense, getUserLimitForUser(expense.getUser(), expenseSheet));
  }

  private void addExpenseToCategoryMap(ExpenseEntity expense, ExpenseSheetEntity expenseSheet) {
    CategoryExpense categoryExpense = expenseSheet.getCategoryExpenseMap().get(expense.getCategory());
    if (categoryExpense == null) {
      categoryExpense = new CategoryExpense(expense.getCategory());
      expenseSheet.getCategoryExpenseMap().put(expense.getCategory(), categoryExpense);
    }
    categoryExpense.addExpense(expense);
  }

  private void addExpenseToUserLimitMap(ExpenseEntity expense, ExpenseSheetEntity expenseSheet) {
    UserLimitEntity userLimit = getUserLimitForUser(expense.getUser(), expenseSheet);
    UserLimitExpense userLimitExpense = expenseSheet.getUserLimitExpenseMap().get(userLimit);
    if (userLimitExpense == null) {
      userLimitExpense = new UserLimitExpense(userLimit);
      expenseSheet.getUserLimitExpenseMap().put(userLimitExpense.getUserLimit(), userLimitExpense);
    }
    userLimitExpense.addExpense(expense);
  }

  public void removeExpense(ExpenseEntity expense, ExpenseSheetEntity expenseSheet) {
    expenseSheet.getExpenseList().remove(expense);
    removeExpenseFromMap(expense, expenseSheet);
  }

  private void removeExpenseFromMap(ExpenseEntity expense, ExpenseSheetEntity expenseSheet) {
    DateExpense dateExpense = expenseSheet.getDateExpenseMap().get(expense.getDate());
    if (dateExpense != null)
      dateExpense.removeExpense(expense);
  }

  public UserLimitEntity getUserLimitForUser(UserEntity user, ExpenseSheetEntity expenseSheet) {
    Optional<UserLimitEntity> result = expenseSheet.getUserLimitList().parallelStream()
            .filter(ul -> ul.getUser().equals(user))
            .findFirst();
    return result.orElse(null);
  }

  public Set<String> getAllComments(ExpenseSheetEntity expenseSheet) {
    return expenseSheet.getExpenseList().stream()
            .filter(e -> e.getComment() != null && !e.getComment().isEmpty())
            .map(ExpenseEntity::getComment)
            .collect(Collectors.toSet());
  }

  public Set<String> getCommentForCategory(ExpenseSheetEntity expenseSheet, CategoryEntity category) {
    return expenseSheet.getExpenseList().stream()
            .filter(e -> e.getCategory().equals(category))
            .filter(e -> e.getComment() != null && !e.getComment().isEmpty())
            .map(ExpenseEntity::getComment)
            .collect(Collectors.toSet());
  }

  public List<String> getYearList(ExpenseSheetEntity expenseSheet) {
    List<String> yearList = new ArrayList<>();
    int firstYear = expenseSheet.getExpenseList().parallelStream()
            .mapToInt(e -> e.getDate().getYear())
            .min()
            .orElse(LocalDate.now().getYear());
    int lastYear = expenseSheet.getExpenseList().parallelStream()
            .mapToInt(e -> e.getDate().getYear())
            .max()
            .orElse(firstYear);

    for (int i = firstYear; i <= lastYear; i++) {
      yearList.add(Integer.toString(i));
    }
    return yearList;
  }

  public DateExpense getDateExpenseMap(ExpenseSheetEntity expenseSheet, LocalDate date) {
    if (date.isBefore(expenseSheet.getFirstDate()) || date.isAfter(expenseSheet.getLastDate())) {
      prepareExpenseMap(expenseSheet, date.withDayOfMonth(1), date.withDayOfMonth(date.lengthOfMonth()), null, null);
    }
    return expenseSheet.getDateExpenseMap().get(date);
  }

  public CategoryExpense getCategoryExpenseMap(ExpenseSheetEntity expenseSheet, CategoryEntity category) {
    return expenseSheet.getCategoryExpenseMap().get(category);
  }

  public List<YearCategory> prepareYearCategoryList(ExpenseSheetEntity expenseSheet) {
    LocalDateTime stopper = LocalDateTime.now();
    List<YearCategory> yearCategoryList = new ArrayList<>();
    LocalDate firstDay = LocalDate.now();
    for (String year : getYearList(expenseSheet)) {
      YearCategory yearCategory = new YearCategory(Integer.parseInt(year), expenseSheet.getCategoryList());
      firstDay = firstDay.withYear(Integer.parseInt(year)).withDayOfMonth(1);
      for (int m = 1; m <= 12; m++) {
        firstDay = firstDay.withMonth(m);
        prepareExpenseMap(expenseSheet, firstDay, firstDay.withDayOfMonth(firstDay.lengthOfMonth()), firstDay, firstDay.withDayOfMonth(firstDay.lengthOfMonth()));
        for (CategoryEntity category : expenseSheet.getCategoryList()) {
          CategoryExpense categoryExpense = getCategoryExpenseMap(expenseSheet, category);
          if (categoryExpense != null)
            yearCategory.getCategory((m-1), categoryExpense.getCategory()).setSum(categoryExpense.getSum());
        }
      }
      yearCategoryList.add(yearCategory);
    }
    log.info("prepareYearCategoryList for {} finish: {} ms", expenseSheet, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return yearCategoryList;
  }


  public List<UserLimitEntity> getUserLimitListDesc(ExpenseSheetEntity expenseSheet) {
    List<UserLimitEntity> returnList = new ArrayList<>(expenseSheet.getUserLimitList());
    Collections.reverse(returnList);
    return returnList;
  }

  public List<UserLimitEntity> getUserLimitListRealUser(ExpenseSheetEntity expenseSheet) {
    return expenseSheet.getUserLimitList().parallelStream()
            .filter(uL -> uL.getUser() instanceof RealUserEntity)
            .collect(Collectors.toList());
  }

  public List<UserLimitEntity> getUserLimitListNotRealUser(ExpenseSheetEntity expenseSheet) {
    List<UserLimitEntity> userLimitList = new ArrayList<>();
    for (UserLimitEntity userLimit : expenseSheet.getUserLimitList())
      if (!(userLimit.getUser() instanceof RealUserEntity))
        userLimitList.add(userLimit);
    return userLimitList;
  }

  public List<UserSumChange> checkSummary(ExpenseSheetEntity expenseSheet, LocalDate date) {
    List<UserSumChange> returnList = new ArrayList<>();
    LocalDateTime stopper = LocalDateTime.now();
    if (expenseSheet.getFilter() != null)
      return null;
    for (UserLimitEntity userLimit : expenseSheet.getUserLimitList()) {
      uls.fetchUserSummaryList(userLimit);
      log.debug("checkSummary for: {} at {}", userLimit, date);
      UserSummaryEntity userSummary = uss.findUserSummary(userLimit, date);
      if (userSummary == null) {
        userSummary = uss.create(userLimit, date);
        userLimit.getUserSummaryList().add(userSummary);
        uls.merge(userLimit);
      }
      BigDecimal exSummary = new BigDecimal(0);
      if (expenseSheet.getUserLimitExpenseMap().get(userLimit) != null)
        exSummary = expenseSheet.getUserLimitExpenseMap().get(userLimit).getSum();
      log.debug("exSummary: {}; userSummary: {}", exSummary, userSummary.getSum());
      if (userSummary.getSum().compareTo(exSummary) != 0) {
        returnList.add(new UserSumChange(userLimit.getUser().getName(), exSummary, userSummary.getSum()));
        userSummary.setSum(exSummary);
        uss.merge(userSummary);
      }
    }
    log.info("checkSummary for {} finish: {} ms", expenseSheet, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return returnList;
  }
}
