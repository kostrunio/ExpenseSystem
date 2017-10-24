package pl.kostro.expensesystem;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.Expense;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.RealUser;
import pl.kostro.expensesystem.model.User;
import pl.kostro.expensesystem.model.UserLimit;
import pl.kostro.expensesystem.model.UserSummary;
import pl.kostro.expensesystem.model.mongodb.NewCategory;
import pl.kostro.expensesystem.model.mongodb.NewExpense;
import pl.kostro.expensesystem.model.mongodb.NewExpenseSheet;
import pl.kostro.expensesystem.model.mongodb.NewRealUser;
import pl.kostro.expensesystem.model.mongodb.NewUser;
import pl.kostro.expensesystem.model.mongodb.NewUserLimit;
import pl.kostro.expensesystem.model.mongodb.NewUserSummary;
import pl.kostro.expensesystem.model.mongodb.repository.NewCategoryRepository;
import pl.kostro.expensesystem.model.mongodb.repository.NewExpenseRepository;
import pl.kostro.expensesystem.model.mongodb.repository.NewExpenseSheetRepository;
import pl.kostro.expensesystem.model.mongodb.repository.NewRealUserRepository;
import pl.kostro.expensesystem.model.mongodb.repository.NewUserLimitRepository;
import pl.kostro.expensesystem.model.mongodb.repository.NewUserRepository;
import pl.kostro.expensesystem.model.mongodb.repository.NewUserSummaryRepository;
import pl.kostro.expensesystem.model.repository.RealUserRepository;

@SpringBootApplication
public class CopyFromPostgresToMongoDb implements CommandLineRunner {

  @Autowired
  private RealUserRepository rur;
  @Autowired
  private NewRealUserRepository newRur;
  @Autowired
  private NewExpenseSheetRepository newEshr;
  @Autowired
  private NewCategoryRepository newCr;
  @Autowired
  private NewUserRepository newUr;
  @Autowired
  private NewUserLimitRepository newUlr;
  @Autowired
  private NewUserSummaryRepository newUsr;
  @Autowired
  private NewExpenseRepository newEr;

  private static Logger logger = LogManager.getLogger();

  public static void main(String[] args) {
    SpringApplication.run(CopyFromPostgresToMongoDb.class, args);
  }

  @Override
  @Transactional
  public void run(String... arg0) throws Exception {
    for (RealUser realUser : rur.findAll()) {
      if (realUser.getLogDate() != null && realUser.getLogDate().isAfter(LocalDateTime.of(2017, 2, 1, 0, 0))) {
        NewRealUser newRealUser = new NewRealUser(realUser);
        logger.debug("Creating: {}", newRealUser);
//        newRur.save(newRealUser);
      }
    }
    for (RealUser realUser : rur.findAll()) {
      if (realUser.getLogDate() != null && realUser.getLogDate().isAfter(LocalDateTime.of(2017, 2, 1, 0, 0))) {
        logger.debug("Finding: {} in mongoDB", realUser);
        NewRealUser newRealUser = null;
//        newRealUser = newRur.findByName(realUser.getName());
        logger.debug("NewRealUser found: {}", newRealUser);
        for (ExpenseSheet expenseSheet : realUser.getExpenseSheetList()) {
          if (expenseSheet.getOwner().equals(realUser)) {
            NewExpenseSheet newExpenseSheet = new NewExpenseSheet(expenseSheet, newRealUser);
            logger.debug("\tCreating: {}", newExpenseSheet);
//            newEshr.save(newExpenseSheet);
            for (Category category : expenseSheet.getCategoryList()) {
              NewCategory newCategory = new NewCategory(category);
              logger.debug("\t\tCreating: {}", newCategory);
//              newCr.save(newCategory);
              newExpenseSheet.getCategoryList().add(newCategory);
            }
//            newEshr.save(newExpenseSheet);
            for (UserLimit userLimit : expenseSheet.getUserLimitList()) {
              NewRealUser newRealUser2 = null;
              NewUser newUser2 = null;
              if (userLimit.getUser() instanceof RealUser) {
                if (!userLimit.getUser().equals(realUser)) {
                  logger.debug("\t\tFinding by name in mongoDB: {}", userLimit.getUser());
//                  newRealUser2 = newRur.findByName(userLimit.getUser().getName());
                  logger.debug("newRealUser2 found: {}", newRealUser2);
                }
              } else if (userLimit.getUser() instanceof User) {
                newUser2 = new NewUser(userLimit.getUser());
                logger.debug("\t\tCreating: {}", newUser2);
//                newUr.save(newUser2);
              }
              NewUserLimit newUserLimit = new NewUserLimit(userLimit, newRealUser2, newUser2);
              logger.debug("\t\tCreating: {}", newUserLimit);
//              newUlr.save(newUserLimit);
              newExpenseSheet.getUserLimitList().add(newUserLimit);
              for (UserSummary userSummary : userLimit.getUserSummaryList()) {
                NewUserSummary newUserSummary = new NewUserSummary(userSummary);
                logger.debug("\t\t\tCreating: {}", newUserSummary);
//                newUsr.save(newUserSummary);
                newUserLimit.getUserSummaryList().add(newUserSummary);
              }
//              newUlr.save(newUserLimit);
            }
//            newEshr.save(newExpenseSheet);
            for (Expense expense : expenseSheet.getExpenseList()) {
              logger.debug("\t\tFinding in mongoDB: c={}, u={}", expense.getCategory(), expense.getUser());
              NewCategory newCategory = null;
              Optional<NewCategory> result = newExpenseSheet.getCategoryList().stream()
                  .filter(c -> c.getName().equals(expense.getCategory().getName()))
                  .findFirst();
              if (result.isPresent()) newCategory = result.get();
              NewUser newUser = null;
              Optional<NewUserLimit> result2 = newExpenseSheet.getUserLimitList().stream()
                  .filter(ul -> ul.getUser().getName().equals(expense.getUser().getName()))
                  .findFirst();
              if (result2.isPresent()) newUser = result2.get().getUser();
              logger.debug("\t\tnewCategory found: {}, newUser found:{}", newCategory, newUser);
              NewExpense newExpense = new NewExpense(expense, newCategory, newUser, newExpenseSheet);
              logger.debug("\t\tCreating: {}", newExpense);
//              newEr.save(newExpense);
              newExpenseSheet.getExpenseList().add(newExpense);
            }
//            newEshr.save(newExpenseSheet);
          }
        }
      }
    }
  }

}
