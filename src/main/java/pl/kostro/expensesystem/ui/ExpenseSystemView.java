package pl.kostro.expensesystem.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import pl.kostro.expensesystem.model.entity.CategoryEntity;
import pl.kostro.expensesystem.model.entity.ExpenseEntity;
import pl.kostro.expensesystem.model.entity.ExpenseSheetEntity;
import pl.kostro.expensesystem.model.entity.RealUserEntity;
import pl.kostro.expensesystem.model.entity.UserEntity;
import pl.kostro.expensesystem.model.entity.UserLimitEntity;
import pl.kostro.expensesystem.model.entity.UserSummaryEntity;
import pl.kostro.expensesystem.model.repository.RealUserRepository;
import pl.kostro.expensesystem.model.service.CategoryService;
import pl.kostro.expensesystem.model.service.ExpenseService;
import pl.kostro.expensesystem.model.service.ExpenseSheetService;
import pl.kostro.expensesystem.model.service.RealUserService;
import pl.kostro.expensesystem.model.service.UserLimitService;
import pl.kostro.expensesystem.model.service.UserService;
import pl.kostro.expensesystem.model.service.UserSummaryService;
import pl.kostro.expensesystem.utils.calculator.Calculator;
import pl.kostro.expensesystem.utils.mail.SendEmail;
import pl.kostro.expensesystem.utils.transform.service.ExpenseSheetTransformService;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

@Route("")
@PageTitle("ExpenseSystem")
@PermitAll
public class ExpenseSystemView extends VerticalLayout {

    private final RealUserService realUserService;
    private final RealUserRepository realUserRepository;
    private final ExpenseSheetService expenseSheetService;
    private final ExpenseService expenseService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final UserLimitService userLimitService;
    private final UserSummaryService userSummaryService;
    private final ExpenseSheetTransformService transformService;

    private final VerticalLayout page = new VerticalLayout();
    private final DatePicker selectedDate = new DatePicker("Date");
    private final DatePicker monthPicker = new DatePicker("Month");
    private final Tabs tabs = new Tabs();
    private final Div content = new Div();
    private ExpenseSheetEntity sheet;
    private String currentSection = "Day";
    private int chartYear;
    private final Set<CategoryEntity> chartCategories = new HashSet<>();
    private final Set<UserLimitEntity> chartUsers = new HashSet<>();

    @Autowired
    public ExpenseSystemView(RealUserService realUserService, RealUserRepository realUserRepository,
                             ExpenseSheetService expenseSheetService, ExpenseService expenseService,
                             CategoryService categoryService, UserService userService,
                             UserLimitService userLimitService, UserSummaryService userSummaryService,
                             ExpenseSheetTransformService transformService) {
        this.realUserService = realUserService;
        this.realUserRepository = realUserRepository;
        this.expenseSheetService = expenseSheetService;
        this.expenseService = expenseService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.userLimitService = userLimitService;
        this.userSummaryService = userSummaryService;
        this.transformService = transformService;
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        page.setWidthFull();
        page.setMaxWidth("1280px");
        page.setPadding(true);
        page.setSpacing(true);
        add(page);
        setAlignItems(Alignment.CENTER);
        selectedDate.setValue(LocalDate.now());
        monthPicker.setValue(LocalDate.now().withDayOfMonth(1));
        monthPicker.setI18n(new DatePicker.DatePickerI18n().setDateFormat("MMMM yyyy"));
        selectedDate.addValueChangeListener(event -> {
            if (event.getValue() != null && isAuthenticated()) {
                if ("Day".equals(currentSection)) showDay();
                if ("Month".equals(currentSection)) showMonth();
            }
        });
        monthPicker.addValueChangeListener(event -> {
            if (event.getValue() != null && isAuthenticated() && "Month".equals(currentSection)) showMonth();
        });
        render();
    }

    private void render() {
        page.removeAll();
        if (!isAuthenticated()) {
            showLogin();
        } else {
            showWorkspace();
        }
    }

    private boolean isAuthenticated() {
        return VaadinSession.getCurrent().getAttribute(RealUserEntity.class) != null;
    }

    private RealUserEntity currentUser() {
        return VaadinSession.getCurrent().getAttribute(RealUserEntity.class);
    }

    private void showLogin() {
        page.add(new H2("ExpenseSystem"));
        page.add(new Paragraph("Track expenses, manage shared sheets, and review spending summaries."));
        Tabs modes = new Tabs(new Tab("Sign in"), new Tab("Create account"));
        VerticalLayout form = new VerticalLayout();
        form.setWidthFull();
        TextField username = new TextField("Username");
        PasswordField password = new PasswordField("Password");
        TextField email = new TextField("Email");
        PasswordField repeatedPassword = new PasswordField("Repeat password");
        Button submit = new Button("Sign in");
        email.setVisible(false);
        repeatedPassword.setVisible(false);
        modes.addSelectedChangeListener(event -> {
            boolean register = modes.getSelectedTab().getLabel().equals("Create account");
            email.setVisible(register);
            repeatedPassword.setVisible(register);
            submit.setText(register ? "Create account" : "Sign in");
        });
        submit.addClickListener(event -> {
            String name = username.getValue().trim();
            String pass = password.getValue();
            if (name.isBlank() || pass.isBlank()) {
                error("Enter your username and password.");
                return;
            }
            if (email.isVisible()) {
                if (!pass.equals(repeatedPassword.getValue())) {
                    error("The passwords do not match.");
                    return;
                }
                if (realUserService.findRealUser(name) != null) {
                    error("That username is already registered.");
                    return;
                }
                RealUserEntity created = realUserService.create(name, pass, email.getValue().trim());
                if (created.getEmail() != null && !created.getEmail().isBlank()) {
                    Thread welcomeEmail = new Thread(() -> SendEmail.welcome(created), "expense-system-welcome");
                    welcomeEmail.setDaemon(true);
                    welcomeEmail.start();
                }
                VaadinSession.getCurrent().setAttribute(RealUserEntity.class, created);
                created.setClearPassword(pass);
                openWorkspace();
                return;
            }
            RealUserEntity user = realUserService.getUserData(name, pass);
            if (user == null) {
                error("The username or password is incorrect.");
                return;
            }
            VaadinSession.getCurrent().setAttribute(RealUserEntity.class, user);
            realUserService.fetchExpenseSheetList(user);
            if (user.getDefaultExpenseSheet() != null) {
                activateSheet(user.getDefaultExpenseSheet(), pass);
            }
            openWorkspace();
        });
        form.add(username, password, email, repeatedPassword, submit);
        page.add(modes, form);
    }

    private void openWorkspace() {
        realUserService.fetchExpenseSheetList(currentUser());
        if (sheet == null) {
            List<ExpenseSheetEntity> sheets = currentUser().getExpenseSheetList();
            if (!sheets.isEmpty()) activateSheet(sheets.get(0), currentUser().getClearPassword());
        }
        render();
    }

    private void showWorkspace() {
        RealUserEntity user = currentUser();
        realUserService.fetchExpenseSheetList(user);
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setAlignItems(Alignment.CENTER);
        header.add(new H2("ExpenseSystem"));
        ComboBox<ExpenseSheetEntity> sheets = new ComboBox<>("Expense sheet");
        sheets.setItems(user.getExpenseSheetList());
        sheets.setItemLabelGenerator(ExpenseSheetEntity::getName);
        sheets.setValue(sheet);
        sheets.addValueChangeListener(event -> {
            if (event.getValue() != null && !Objects.equals(event.getValue(), sheet)) {
                activateSheet(event.getValue(), user.getClearPassword());
            }
        });
        Button newSheet = new Button("New sheet", event -> createSheetDialog());
        Button account = new Button("Account", event -> showAccount());
        Button logout = new Button("Sign out", event -> {
            VaadinSession.getCurrent().getSession().invalidate();
            UI.getCurrent().navigate("");
        });
        header.add(sheets, newSheet, account, logout);
        header.expand(header.getComponentAt(0));
        page.add(header);

        if (sheet == null) {
            content.removeAll();
            content.add(new H3("Create an expense sheet to get started"));
            page.add(content);
            return;
        }
        HorizontalLayout navigation = new HorizontalLayout();
        Button day = new Button("Day", event -> showDay());
        Button month = new Button("Month", event -> showMonth());
        Button table = new Button("Expenses", event -> showTable());
        Button charts = new Button("Charts", event -> showCharts());
        Button summaries = new Button("User summaries", event -> showSummaries());
        Button settings = new Button("Settings", event -> showSettings());
        navigation.add(day, month, table, charts, summaries, settings);
        page.add(navigation, content);
        if (content.getChildren().findAny().isEmpty()) showDay();
    }

    private void activateSheet(ExpenseSheetEntity selected, String password) {
        realUserService.fetchExpenseSheetList(currentUser());
        if (currentUser().getExpenseSheetList().stream().noneMatch(owned -> owned.equals(selected))) {
            error("You do not have access to that expense sheet.");
            return;
        }
        sheet = selected;
        VaadinSession.getCurrent().setAttribute(ExpenseSheetEntity.class, sheet);
        try {
            expenseSheetService.fetchCategoryList(sheet);
            expenseSheetService.fetchExpenseList(sheet);
            expenseSheetService.fetchUserLimitList(sheet);
            for (UserLimitEntity limit : sheet.getUserLimitList()) userLimitService.fetchUserSummaryList(limit);
            if (password == null || password.isBlank()) throw new IllegalArgumentException("Password required");
            sheet.setSecretKey(password);
            validateSheetKey();
            if (!sheet.getEncrypted()) expenseSheetService.encrypt(sheet);
            showDay();
        } catch (RuntimeException ex) {
            sheet.setSecretKey("invalid");
            requestSheetPassword();
        }
    }

    private void validateSheetKey() {
        if (!sheet.getCategoryList().isEmpty()) {
            if (sheet.getCategoryList().get(0).getName() == null) throw new IllegalArgumentException("Invalid sheet key");
        } else if (!sheet.getUserLimitList().isEmpty()) {
            UserLimitEntity limit = sheet.getUserLimitList().get(0);
            if (limit.getLimit() == null) throw new IllegalArgumentException("Invalid sheet key");
        }
    }

    private void requestSheetPassword() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Unlock " + sheet.getName());
        PasswordField field = new PasswordField("Sheet password");
        Button unlock = new Button("Unlock", event -> {
            try {
                sheet.setSecretKey(field.getValue());
                validateSheetKey();
                dialog.close();
                showWorkspaceContent("Day");
            } catch (RuntimeException exception) {
                sheet.setSecretKey("invalid");
                error("Unable to unlock this sheet with that password.");
            }
        });
        dialog.add(new VerticalLayout(new Paragraph("This sheet uses a separate encryption password."), field, unlock));
        dialog.open();
    }

    private void showWorkspaceContent(String section) {
        render();
        currentSection = section;
        switch (section) {
            case "Month" -> showMonth();
            case "Expenses" -> showTable();
            case "Charts" -> showCharts();
            case "User summaries" -> showSummaries();
            case "Settings" -> showSettings();
            default -> showDay();
        }
    }

    private void showDay() {
        currentSection = "Day";
        content.removeAll();
        content.add(new H3("Daily expenses"));
        HorizontalLayout controls = new HorizontalLayout(selectedDate);
        Button add = new Button("Add expense", event -> editExpense(null));
        controls.add(add);
        content.add(controls);
        List<ExpenseEntity> rows = sheet.getExpenseList().stream()
                .filter(expense -> selectedDate.getValue().equals(expense.getDate()))
                .sorted(Comparator.comparing(ExpenseEntity::getId, Comparator.nullsLast(Comparator.reverseOrder())))
                .toList();
        content.add(expenseGrid(rows));
        content.add(new Paragraph("Daily total: " + sum(rows).toPlainString()));
    }

    private void showMonth() {
        currentSection = "Month";
        content.removeAll();
        content.add(new H3("Monthly overview"));
        HorizontalLayout controls = new HorizontalLayout(monthPicker);
        Button previous = new Button("Previous month", event ->
                monthPicker.setValue(monthPicker.getValue().minusMonths(1)));
        Button next = new Button("Next month", event ->
                monthPicker.setValue(monthPicker.getValue().plusMonths(1)));
        controls.add(previous, next);
        content.add(controls);
        LocalDate month = monthPicker.getValue().withDayOfMonth(1);
        List<ExpenseEntity> rows = monthExpenses(month);
        content.add(new Paragraph(month.getMonth().getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault())
                + " " + month.getYear() + " total: " + sum(rows).toPlainString()));
        content.add(new H4("Category totals"));
        com.vaadin.flow.component.grid.Grid<CategoryTotal> categoryTotals =
                new com.vaadin.flow.component.grid.Grid<>();
        categoryTotals.addColumn(CategoryTotal::name).setHeader("Category");
        categoryTotals.addColumn(CategoryTotal::total).setHeader("Total");
        categoryTotals.setItems(sheet.getCategoryList().stream().map(category -> new CategoryTotal(category.getName(),
                rows.stream().filter(expense -> category.equals(expense.getCategory()))
                        .map(expense -> expense.getValue().multiply(category.getMultiplier()))
                        .reduce(BigDecimal.ZERO, BigDecimal::add))).toList());
        categoryTotals.setWidthFull();
        content.add(categoryTotals);
        content.add(new H4("User budgets"));
        com.vaadin.flow.component.grid.Grid<UserBudget> budgets = new com.vaadin.flow.component.grid.Grid<>();
        budgets.addColumn(UserBudget::name).setHeader("User");
        budgets.addColumn(UserBudget::spent).setHeader("Spent this month");
        budgets.addColumn(UserBudget::remaining).setHeader("Remaining");
        budgets.setItems(sheet.getUserLimitList().stream().map(limit -> {
            BigDecimal spent = rows.stream().filter(expense -> limit.getUser().equals(expense.getUser()))
                    .map(expense -> expense.getValue().multiply(expense.getCategory().getMultiplier()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            return new UserBudget(limit.getUser().getName(), spent, limit.getLimit().subtract(spent));
        }).toList());
        budgets.setWidthFull();
        content.add(budgets);
        for (int day = 1; day <= month.lengthOfMonth(); day++) {
            LocalDate date = month.withDayOfMonth(day);
            List<ExpenseEntity> dayRows = rows.stream().filter(expense -> expense.getDate().equals(date)).toList();
            HorizontalLayout row = new HorizontalLayout();
            row.setWidthFull();
            Button open = new Button(date.format(DateTimeFormatter.ISO_LOCAL_DATE) + " · "
                    + date.getDayOfWeek().getDisplayName(java.time.format.TextStyle.SHORT, Locale.getDefault()),
                    event -> {
                        selectedDate.setValue(date);
                        showDay();
                    });
            open.setWidth("260px");
            row.add(open, new Span(dayRows.size() + " expenses"), new Span(sum(dayRows).toPlainString()));
            row.addClassName("month-day-row");
            content.add(row);
        }
    }

    private List<ExpenseEntity> monthExpenses(LocalDate month) {
        return sheet.getExpenseList().stream()
                .filter(expense -> !expense.getDate().isBefore(month.withDayOfMonth(1)))
                .filter(expense -> !expense.getDate().isAfter(month.withDayOfMonth(month.lengthOfMonth())))
                .sorted(Comparator.comparing(ExpenseEntity::getDate).reversed())
                .toList();
    }

    private Component expenseGrid(List<ExpenseEntity> expenses) {
        com.vaadin.flow.component.grid.Grid<ExpenseEntity> grid = new com.vaadin.flow.component.grid.Grid<>();
        grid.setItems(expenses);
        grid.addColumn(ExpenseEntity::getDate).setHeader("Date").setAutoWidth(true);
        grid.addColumn(expense -> expense.getCategory() == null ? "" : expense.getCategory().getName())
                .setHeader("Category").setAutoWidth(true);
        grid.addColumn(expense -> expense.getUser() == null ? "" : expense.getUser().getName())
                .setHeader("User").setAutoWidth(true);
        grid.addColumn(ExpenseEntity::getFormula).setHeader("Formula").setAutoWidth(true);
        grid.addColumn(ExpenseEntity::getValue).setHeader("Value").setAutoWidth(true);
        grid.addColumn(ExpenseEntity::getComment).setHeader("Comment").setFlexGrow(1);
        grid.addColumn(ExpenseEntity::isNotify).setHeader("Notify").setAutoWidth(true);
        grid.addComponentColumn(expense -> new Button("Edit", event -> editExpense(expense))).setHeader("Actions");
        grid.setWidthFull();
        grid.setAllRowsVisible(!expenses.isEmpty() && expenses.size() < 12);
        return grid;
    }

    private void editExpense(ExpenseEntity existing) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle(existing == null ? "Add expense" : "Edit expense");
        DatePicker date = new DatePicker("Date");
        date.setValue(existing == null ? selectedDate.getValue() : existing.getDate());
        ComboBox<CategoryEntity> category = new ComboBox<>("Category");
        category.setItems(sheet.getCategoryList());
        category.setItemLabelGenerator(CategoryEntity::getName);
        if (existing != null) category.setValue(existing.getCategory());
        else if (!sheet.getCategoryList().isEmpty()) category.setValue(sheet.getCategoryList().get(0));
        ComboBox<UserLimitEntity> user = new ComboBox<>("User");
        user.setItems(sheet.getUserLimitList());
        user.setItemLabelGenerator(limit -> limit.getUser().getName());
        if (existing != null) {
            UserLimitEntity limit = transformService.getUserLimitForUser(existing.getUser(), sheet);
            if (limit != null) user.setValue(limit);
        } else if (!sheet.getUserLimitList().isEmpty()) user.setValue(sheet.getUserLimitList().get(0));
        TextField formula = new TextField("Formula");
        formula.setPlaceholder("For example: 12.50 or 2 + 3");
        formula.setValue(existing == null || existing.getFormula() == null ? "" : existing.getFormula());
        TextField comment = new TextField("Comment");
        comment.setValue(existing == null || existing.getComment() == null ? "" : existing.getComment());
        Checkbox notify = new Checkbox("Send notification");
        notify.setValue(existing != null && existing.isNotify());
        Button save = new Button("Save", event -> {
            if (date.getValue() == null || category.getValue() == null || user.getValue() == null
                    || formula.getValue().isBlank() || !Calculator.verifyAllowed(formula.getValue())) {
                error("Choose a date, category, user, and valid formula.");
                return;
            }
            try {
                Calculator.getOperationResult(formula.getValue());
            } catch (RuntimeException invalidFormula) {
                error("Enter a valid expense formula.");
                return;
            }
            ExpenseEntity expense = existing == null ? new ExpenseEntity() : existing;
            expense.setDate(date.getValue());
            expense.setCategory(category.getValue());
            expense.setUser(user.getValue().getUser());
            expense.setFormula(formula.getValue().startsWith("=")
                    ? formula.getValue().substring(1) : formula.getValue());
            expense.setComment(comment.getValue());
            expense.setNotify(notify.getValue());
            expense.setExpenseSheet(sheet);
            expenseService.save(expense);
            if (existing == null && sheet.getExpenseList().stream().noneMatch(item -> item.equals(expense))) {
                sheet.getExpenseList().add(expense);
            }
            dialog.close();
            refreshSection();
        });
        Button remove = new Button("Delete", event -> {
            if (existing != null) {
                expenseService.remove(existing);
                sheet.getExpenseList().remove(existing);
                dialog.close();
                refreshSection();
            }
        });
        remove.setVisible(existing != null);
        Button cancel = new Button("Cancel", event -> dialog.close());
        dialog.add(new VerticalLayout(date, category, user, formula, comment, notify,
                new HorizontalLayout(save, remove, cancel)));
        dialog.open();
    }

    private void showTable() {
        currentSection = "Expenses";
        content.removeAll();
        content.add(new H3("All expenses"));
        DatePicker from = new DatePicker("From");
        DatePicker to = new DatePicker("To");
        LocalDate month = selectedDate.getValue().withDayOfMonth(1);
        from.setValue(month);
        to.setValue(month.withDayOfMonth(month.lengthOfMonth()));
        ComboBox<CategoryEntity> category = new ComboBox<>("Category");
        category.setItems(sheet.getCategoryList());
        category.setItemLabelGenerator(CategoryEntity::getName);
        ComboBox<UserLimitEntity> user = new ComboBox<>("User");
        user.setItems(sheet.getUserLimitList());
        user.setItemLabelGenerator(limit -> limit.getUser().getName());
        TextField formula = new TextField("Formula contains");
        TextField comment = new TextField("Comment contains");
        com.vaadin.flow.component.grid.Grid<ExpenseEntity> grid = new com.vaadin.flow.component.grid.Grid<>();
        grid.addColumn(ExpenseEntity::getDate).setHeader("Date").setAutoWidth(true);
        grid.addColumn(expense -> expense.getCategory().getName()).setHeader("Category").setAutoWidth(true);
        grid.addColumn(expense -> expense.getUser().getName()).setHeader("User").setAutoWidth(true);
        grid.addColumn(ExpenseEntity::getFormula).setHeader("Formula");
        grid.addColumn(ExpenseEntity::getValue).setHeader("Value").setAutoWidth(true);
        grid.addColumn(ExpenseEntity::getComment).setHeader("Comment");
        grid.addComponentColumn(expense -> new Button("Edit", event -> editExpense(expense))).setHeader("Actions");
        grid.setWidthFull();
        Paragraph total = new Paragraph();
        List<ExpenseEntity> exportRows = new ArrayList<>();
        Runnable update = () -> {
            List<ExpenseEntity> matches = sheet.getExpenseList().stream()
                    .filter(expense -> from.getValue() == null || !expense.getDate().isBefore(from.getValue()))
                    .filter(expense -> to.getValue() == null || !expense.getDate().isAfter(to.getValue()))
                    .filter(expense -> category.getValue() == null || expense.getCategory().equals(category.getValue()))
                    .filter(expense -> user.getValue() == null || expense.getUser().equals(user.getValue().getUser()))
                    .filter(expense -> formula.getValue().isBlank()
                            || expense.getFormula().toLowerCase(Locale.ROOT).contains(formula.getValue().toLowerCase(Locale.ROOT)))
                    .filter(expense -> comment.getValue().isBlank() || (expense.getComment() != null
                            && expense.getComment().toLowerCase(Locale.ROOT)
                            .contains(comment.getValue().toLowerCase(Locale.ROOT))))
                    .sorted(Comparator.comparing(ExpenseEntity::getDate).reversed()).toList();
            grid.setItems(matches);
            exportRows.clear();
            exportRows.addAll(matches);
            total.setText("Rows: " + matches.size() + " · Total: " + sum(matches));
        };
        from.addValueChangeListener(event -> update.run());
        to.addValueChangeListener(event -> update.run());
        category.addValueChangeListener(event -> update.run());
        user.addValueChangeListener(event -> update.run());
        formula.addValueChangeListener(event -> update.run());
        comment.addValueChangeListener(event -> update.run());
        HorizontalLayout filters = new HorizontalLayout(from, to, category, user, formula, comment);
        filters.setWidthFull();
        Button add = new Button("Add expense", event -> editExpense(null));
        Anchor export = csvExport(exportRows);
        content.add(filters, new HorizontalLayout(add, export), total, grid);
        update.run();
    }

    private Anchor csvExport(List<ExpenseEntity> expenses) {
        StreamResource resource = new StreamResource("expenses.csv",
                () -> new ByteArrayInputStream(buildCsv(expenses).getBytes(StandardCharsets.UTF_8)));
        Anchor download = new Anchor(resource, "Export CSV");
        download.getElement().setAttribute("download", true);
        return download;
    }

    private String buildCsv(List<ExpenseEntity> expenses) {
        StringBuilder csv = new StringBuilder("Date,Category,User,Formula,Value,Comment,Notify\r\n");
        for (ExpenseEntity expense : expenses) {
            csv.append(csv(expense.getDate().toString())).append(',')
                    .append(csv(expense.getCategory().getName())).append(',')
                    .append(csv(expense.getUser().getName())).append(',')
                    .append(csv(expense.getFormula())).append(',')
                    .append(csv(expense.getValue().toString())).append(',')
                    .append(csv(expense.getComment())).append(',')
                    .append(expense.isNotify()).append("\r\n");
        }
        return csv.toString();
    }

    private String csv(String value) {
        String text = value == null ? "" : value;
        if (text.matches("^[\\t\\r].*") || text.stripLeading().matches("^[=+\\-@].*")) {
            text = "'" + text;
        }
        return "\"" + text.replace("\"", "\"\"") + "\"";
    }

    private void showCharts() {
        currentSection = "Charts";
        content.removeAll();
        content.add(new H3("Spending charts"));
        NumberField yearField = new NumberField("Year");
        yearField.setStep(1);
        yearField.setMin(1);
        yearField.setMax(9999);
        if (chartYear == 0) chartYear = selectedDate.getValue().getYear();
        yearField.setValue((double) chartYear);
        MultiSelectComboBox<CategoryEntity> categories = new MultiSelectComboBox<>("Categories");
        categories.setItems(sheet.getCategoryList());
        categories.setItemLabelGenerator(CategoryEntity::getName);
        categories.setValue(chartCategories);
        MultiSelectComboBox<UserLimitEntity> users = new MultiSelectComboBox<>("Users");
        users.setItems(sheet.getUserLimitList());
        users.setItemLabelGenerator(limit -> limit.getUser().getName());
        users.setValue(chartUsers);
        HorizontalLayout filters = new HorizontalLayout(yearField, categories, users);
        filters.setWidthFull();
        content.add(filters);
        int year = yearField.getValue().intValue();
        List<ExpenseEntity> chartExpenses = chartExpenses(year, categories.getValue(), users.getValue());
        content.add(new Paragraph("Monthly spending by category · " + year));
        BigDecimal max = BigDecimal.ZERO;
        for (int month = 1; month <= 12; month++) {
            int selectedMonth = month;
            for (CategoryEntity item : sheet.getCategoryList()) {
                if (!categories.isEmpty() && !categories.getValue().contains(item)) continue;
                BigDecimal value = chartExpenses.stream()
                        .filter(expense -> expense.getDate().getMonthValue() == selectedMonth)
                        .filter(expense -> item.equals(expense.getCategory()))
                        .map(expense -> expense.getValue().multiply(item.getMultiplier()))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                if (value.compareTo(max) > 0) max = value;
            }
        }
        for (int month = 0; month < 12; month++) {
            HorizontalLayout row = new HorizontalLayout();
            row.setWidthFull();
            row.add(new Span(Month.of(month + 1).getDisplayName(java.time.format.TextStyle.SHORT, Locale.getDefault())));
            for (CategoryEntity item : sheet.getCategoryList()) {
                if (!categories.isEmpty() && !categories.getValue().contains(item)) continue;
                int selectedMonth = month + 1;
                BigDecimal value = chartExpenses.stream()
                        .filter(expense -> expense.getDate().getMonthValue() == selectedMonth)
                        .filter(expense -> item.equals(expense.getCategory()))
                        .map(expense -> expense.getValue().multiply(item.getMultiplier()))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                Div bar = new Div();
                double ratio = max.signum() == 0 ? 0 : value.doubleValue() / max.doubleValue();
                bar.getStyle().set("height", "18px").set("width", Math.max(2, ratio * 220) + "px")
                        .set("background", colorFor(item)).set("border-radius", "4px");
                bar.getElement().setAttribute("title", item.getName() + ": " + value);
                row.add(new Span(item.getName()), bar, new Span(value.toPlainString()));
            }
            content.add(row);
        }
        content.add(new H4("Year totals by category"));
        for (CategoryEntity category : sheet.getCategoryList()) {
            if (!categories.isEmpty() && !categories.getValue().contains(category)) continue;
            BigDecimal total = chartExpenses.stream().filter(expense -> category.equals(expense.getCategory()))
                    .map(expense -> expense.getValue().multiply(category.getMultiplier()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            content.add(new Paragraph(category.getName() + ": " + total.toPlainString()));
        }
        yearField.addValueChangeListener(event -> {
            if (event.getValue() != null && event.getValue() >= 1 && event.getValue() <= 9999) {
                chartYear = event.getValue().intValue();
                showCharts();
            }
        });
        categories.addValueChangeListener(event -> {
            chartCategories.clear();
            chartCategories.addAll(event.getValue());
            showCharts();
        });
        users.addValueChangeListener(event -> {
            chartUsers.clear();
            chartUsers.addAll(event.getValue());
            showCharts();
        });
    }

    private List<ExpenseEntity> chartExpenses(int year, Set<CategoryEntity> categories,
                                               Set<UserLimitEntity> users) {
        return sheet.getExpenseList().stream()
                .filter(expense -> expense.getDate().getYear() == year)
                .filter(expense -> categories.isEmpty() || categories.contains(expense.getCategory()))
                .filter(expense -> users.isEmpty() || users.stream()
                        .anyMatch(limit -> limit.getUser().equals(expense.getUser())))
                .toList();
    }

    private String colorFor(CategoryEntity category) {
        int hue = Math.floorMod(Objects.hashCode(category.getName()), 360);
        return "hsl(" + hue + ", 60%, 52%)";
    }

    private void showSummaries() {
        currentSection = "User summaries";
        content.removeAll();
        content.add(new H3("User summaries and limits"));
        com.vaadin.flow.component.grid.Grid<UserSummaryRow> grid = new com.vaadin.flow.component.grid.Grid<>();
        grid.addColumn(row -> row.user()).setHeader("User").setAutoWidth(true);
        grid.addColumn(row -> row.summary().getDate()).setHeader("Date").setAutoWidth(true);
        grid.addColumn(row -> row.summary().getLimit()).setHeader("Limit").setAutoWidth(true);
        grid.addColumn(row -> row.summary().getSum()).setHeader("Spent").setAutoWidth(true);
        grid.addColumn(row -> row.summary().getLimit().subtract(row.summary().getSum()))
                .setHeader("Remaining").setAutoWidth(true);
        grid.addComponentColumn(row -> new Button("Edit", event -> editSummary(row.summary())))
                .setHeader("Actions");
        List<UserSummaryRow> rows = new ArrayList<>();
        for (UserLimitEntity limit : sheet.getUserLimitList()) {
            userLimitService.fetchUserSummaryList(limit);
            for (UserSummaryEntity summary : limit.getUserSummaryList()) {
                rows.add(new UserSummaryRow(limit.getUser().getName(), summary));
            }
        }
        rows.sort(Comparator.comparing((UserSummaryRow row) -> row.summary().getDate()).reversed());
        grid.setItems(rows);
        grid.setWidthFull();
        content.add(grid);
        content.add(new Paragraph("Monthly expense totals update the stored user summaries as expenses are entered."));
    }

    private void showSettings() {
        currentSection = "Settings";
        content.removeAll();
        content.add(new H3("Expense sheet settings"));
        TextField sheetName = new TextField("Sheet name");
        sheetName.setValue(sheet.getName());
        Button saveName = new Button("Save name", event -> {
            if (sheetName.getValue().isBlank()) {
                error("Sheet name cannot be empty.");
                return;
            }
            sheet.setName(sheetName.getValue().trim());
            expenseSheetService.merge(sheet);
            success("Sheet name saved.");
            showSettings();
        });
        PasswordField newKey = new PasswordField("New sheet password");
        Button changeKey = new Button("Change encryption password", event -> changeSheetPassword(newKey.getValue()));
        Button deleteSheet = new Button("Delete this sheet", event -> confirmDeleteSheet());
        deleteSheet.setEnabled(sheet.getOwner() != null && sheet.getOwner().equals(currentUser()));
        content.add(new HorizontalLayout(sheetName, saveName), new HorizontalLayout(newKey, changeKey, deleteSheet));

        content.add(new H4("Categories"));
        content.add(new Button("Add category", event -> editCategory(null)));
        com.vaadin.flow.component.grid.Grid<CategoryEntity> categories = new com.vaadin.flow.component.grid.Grid<>();
        categories.setItems(sheet.getCategoryList());
        categories.addColumn(CategoryEntity::getName).setHeader("Category").setAutoWidth(true);
        categories.addColumn(CategoryEntity::getMultiplier).setHeader("Multiplier").setAutoWidth(true);
        categories.addComponentColumn(category -> new Button("Edit", event -> editCategory(category)));
        categories.addComponentColumn(category -> {
            Button up = new Button("↑", event -> {
                sheet = expenseSheetService.moveCategoryUp(sheet, category);
                showSettings();
            });
            up.setEnabled(category.getOrder() > 0);
            return up;
        }).setHeader("Order");
        categories.addComponentColumn(category -> {
            Button down = new Button("↓", event -> {
                sheet = expenseSheetService.moveCategoryDown(sheet, category);
                showSettings();
            });
            down.setEnabled(category.getOrder() < sheet.getCategoryList().size() - 1);
            return down;
        });
        categories.addComponentColumn(category -> new Button("Delete", event -> removeCategory(category)));
        categories.setWidthFull();
        content.add(categories);

        content.add(new H4("Users and monthly limits"));
        Button share = new Button("Share with registered user", event -> shareSheet());
        share.setEnabled(sheet.getOwner() != null && sheet.getOwner().equals(currentUser()));
        content.add(new HorizontalLayout(new Button("Add user", event -> addExpenseUser()), share));
        com.vaadin.flow.component.grid.Grid<UserLimitEntity> users = new com.vaadin.flow.component.grid.Grid<>();
        users.setItems(sheet.getUserLimitList());
        users.addColumn(limit -> limit.getUser().getName()).setHeader("User").setAutoWidth(true);
        users.addColumn(UserLimitEntity::getLimit).setHeader("Monthly limit").setAutoWidth(true);
        users.addComponentColumn(limit -> new Button("Edit limit", event -> editLimit(limit)));
        users.addComponentColumn(limit -> new Button("Remove", event -> removeExpenseUser(limit)));
        users.setWidthFull();
        content.add(users);

        if (sheet.getOwner() != null && sheet.getOwner().equals(currentUser())) {
            content.add(new H4("Shared with"));
            com.vaadin.flow.component.grid.Grid<RealUserEntity> collaborators =
                    new com.vaadin.flow.component.grid.Grid<>();
            List<RealUserEntity> sharedUsers = realUserRepository.findUsersWithExpenseSheet(sheet).stream()
                    .filter(sharedUser -> !sharedUser.equals(sheet.getOwner())).toList();
            collaborators.setItems(sharedUsers);
            collaborators.addColumn(RealUserEntity::getName).setHeader("Username").setAutoWidth(true);
            collaborators.addColumn(RealUserEntity::getEmail).setHeader("Email").setAutoWidth(true);
            collaborators.addComponentColumn(sharedUser -> new Button("Remove access", event -> {
                realUserService.removeExpenseSheetAccess(sharedUser, sheet);
                showSettings();
            }));
            collaborators.setWidthFull();
            content.add(collaborators);
        }
    }

    private void editCategory(CategoryEntity category) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle(category == null ? "Add category" : "Edit category");
        TextField name = new TextField("Name");
        NumberField multiplier = new NumberField("Multiplier");
        name.setValue(category == null ? "" : category.getName());
        multiplier.setValue(category == null ? 1.0 : category.getMultiplier().doubleValue());
        Button save = new Button("Save", event -> {
            if (name.getValue().isBlank() || multiplier.getValue() == null) {
                error("Name and multiplier are required.");
                return;
            }
            CategoryEntity changed = category == null
                    ? new CategoryEntity(name.getValue().trim(), sheet.getCategoryList().size()) : category;
            changed.setName(name.getValue().trim());
            changed.setMultiplier(BigDecimal.valueOf(multiplier.getValue()));
            categoryService.save(changed);
            if (category == null) expenseSheetService.addCategory(changed, sheet);
            dialog.close();
            showSettings();
        });
        dialog.add(new VerticalLayout(name, multiplier, new HorizontalLayout(save,
                new Button("Cancel", event -> dialog.close()))));
        dialog.open();
    }

    private void removeCategory(CategoryEntity category) {
        boolean used = sheet.getExpenseList().stream().anyMatch(expense -> category.equals(expense.getCategory()));
        if (used) {
            error("A category used by an expense cannot be deleted.");
            return;
        }
        expenseSheetService.removeCategory(category, sheet);
        categoryService.remove(category);
        showSettings();
    }

    private void addExpenseUser() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Add expense user");
        TextField name = new TextField("Name");
        NumberField limitField = new NumberField("Monthly limit");
        limitField.setValue(0.0);
        Button save = new Button("Save", event -> {
            if (name.getValue().isBlank() || limitField.getValue() == null) {
                error("A name and monthly limit are required.");
                return;
            }
            UserEntity user = userService.createAndSave(name.getValue().trim());
            UserLimitEntity limit = userLimitService.create(user, sheet.getUserLimitList().size());
            limit.setLimit(BigDecimal.valueOf(limitField.getValue()));
            userLimitService.merge(limit);
            sheet.getUserLimitList().add(limit);
            expenseSheetService.merge(sheet);
            dialog.close();
            showSettings();
        });
        dialog.add(new VerticalLayout(name, limitField, new HorizontalLayout(save,
                new Button("Cancel", event -> dialog.close()))));
        dialog.open();
    }

    private void editLimit(UserLimitEntity userLimit) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Monthly limit for " + userLimit.getUser().getName());
        NumberField limit = new NumberField("Limit");
        limit.setValue(userLimit.getLimit().doubleValue());
        Checkbox continuous = new Checkbox("Carry unused balance forward", userLimit.isContinuousSummary());
        Button save = new Button("Save", event -> {
            if (limit.getValue() == null || limit.getValue() < 0) {
                error("Enter a non-negative limit.");
                return;
            }
            userLimit.setLimit(BigDecimal.valueOf(limit.getValue()));
            userLimit.setContinuousSummary(continuous.getValue());
            userLimitService.merge(userLimit);
            dialog.close();
            showSettings();
        });
        dialog.add(new VerticalLayout(limit, continuous, new HorizontalLayout(save,
                new Button("Cancel", event -> dialog.close()))));
        dialog.open();
    }

    private void editSummary(UserSummaryEntity summary) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Edit summary for " + summary.getDate());
        NumberField limit = new NumberField("Limit");
        limit.setValue(summary.getLimit().doubleValue());
        NumberField spent = new NumberField("Spent");
        spent.setValue(summary.getSum().doubleValue());
        Button save = new Button("Save", event -> {
            if (limit.getValue() == null || spent.getValue() == null
                    || limit.getValue() < 0 || spent.getValue() < 0) {
                error("Limit and spent amount must be non-negative.");
                return;
            }
            summary.setLimit(BigDecimal.valueOf(limit.getValue()));
            summary.setSum(BigDecimal.valueOf(spent.getValue()));
            userSummaryService.merge(summary);
            dialog.close();
            showSummaries();
        });
        dialog.add(new VerticalLayout(limit, spent, new HorizontalLayout(save,
                new Button("Cancel", event -> dialog.close()))));
        dialog.open();
    }

    private void removeExpenseUser(UserLimitEntity userLimit) {
        boolean used = sheet.getExpenseList().stream().anyMatch(expense -> userLimit.getUser().equals(expense.getUser()));
        if (sheet.getOwner() != null && sheet.getOwner().equals(userLimit.getUser())) {
            error("The sheet owner cannot be removed from its expense users.");
            return;
        }
        if (used || sheet.getUserLimitList().size() == 1) {
            error(used ? "A user with recorded expenses cannot be removed."
                    : "An expense sheet must have at least one expense user.");
            return;
        }
        sheet.getUserLimitList().remove(userLimit);
        expenseSheetService.merge(sheet);
        userLimitService.remove(userLimit);
        showSettings();
    }

    private void shareSheet() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Share sheet with a registered user");
        TextField username = new TextField("Username");
        Button add = new Button("Share", event -> {
            RealUserEntity other = realUserRepository.findByName(username.getValue().trim());
            if (other == null) {
                error("No registered user has that username.");
                return;
            }
            if (other.equals(currentUser())) {
                error("This sheet already belongs to you.");
                return;
            }
            realUserService.fetchExpenseSheetList(other);
            if (!other.getExpenseSheetList().contains(sheet)) {
                other.getExpenseSheetList().add(sheet);
                realUserService.merge(other, false);
            }
            if (sheet.getUserLimitList().stream().noneMatch(limit -> limit.getUser().equals(other))) {
                UserLimitEntity participant = userLimitService.create(other, sheet.getUserLimitList().size());
                participant.setLimit(BigDecimal.ZERO);
                sheet.getUserLimitList().add(participant);
                expenseSheetService.merge(sheet);
                if (other.getDefaultExpenseSheet() == null) realUserService.setDefaultExpenseSheet(other, sheet);
            }
            dialog.close();
            success("Sheet shared. The recipient will need its encryption password.");
        });
        dialog.add(new VerticalLayout(username, new HorizontalLayout(add,
                new Button("Cancel", event -> dialog.close()))));
        dialog.open();
    }

    private void changeSheetPassword(String newPassword) {
        if (newPassword == null || newPassword.isBlank()) {
            error("Enter a new encryption password.");
            return;
        }
        if (sheet.getOwner() == null || !sheet.getOwner().equals(currentUser())) {
            error("Only the sheet owner can change its encryption password.");
            return;
        }
        try {
            expenseSheetService.decrypt(sheet);
            sheet.setSecretKey(newPassword);
            expenseSheetService.encrypt(sheet);
            success("Encryption password changed.");
            showSettings();
        } catch (RuntimeException exception) {
            error("Unable to change the encryption password.");
        }
    }

    private void confirmDeleteSheet() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Delete " + sheet.getName() + "?");
        dialog.add(new Paragraph("This permanently removes the sheet and its expenses."));
        Button delete = new Button("Delete permanently", event -> {
            realUserService.removeExpenseSheetFromUsers(sheet);
            expenseSheetService.removeExpenseSheet(sheet);
            ExpenseSheetEntity removed = sheet;
            currentUser().getExpenseSheetList().remove(removed);
            sheet = null;
            if (!currentUser().getExpenseSheetList().isEmpty()) {
                ExpenseSheetEntity next = currentUser().getExpenseSheetList().get(0);
                realUserService.setDefaultExpenseSheet(currentUser(), next);
                activateSheet(next, currentUser().getClearPassword());
            } else {
                realUserService.setDefaultExpenseSheet(currentUser(), null);
            }
            dialog.close();
            render();
        });
        dialog.add(new VerticalLayout(new HorizontalLayout(delete,
                new Button("Cancel", event -> dialog.close()))));
        dialog.open();
    }

    private void createSheetDialog() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Create expense sheet");
        TextField name = new TextField("Name");
        PasswordField key = new PasswordField("Encryption password");
        PasswordField repeat = new PasswordField("Repeat password");
        Button create = new Button("Create", event -> {
            if (name.getValue().isBlank() || key.getValue().isBlank() || !key.getValue().equals(repeat.getValue())) {
                error("Enter a name and matching encryption passwords.");
                return;
            }
            ExpenseSheetEntity encryptionContext = new ExpenseSheetEntity();
            encryptionContext.setSecretKey(key.getValue());
            VaadinSession.getCurrent().setAttribute(ExpenseSheetEntity.class, encryptionContext);
            UserLimitEntity firstLimit = userLimitService.create(currentUser(), 0);
            ExpenseSheetEntity created = expenseSheetService.create(name.getValue().trim(), key.getValue(),
                    currentUser(), firstLimit);
            currentUser().getExpenseSheetList().add(created);
            realUserService.merge(currentUser(), false);
            if (currentUser().getDefaultExpenseSheet() == null)
                realUserService.setDefaultExpenseSheet(currentUser(), created);
            dialog.close();
            activateSheet(created, key.getValue());
            render();
        });
        dialog.add(new VerticalLayout(name, key, repeat, new HorizontalLayout(create,
                new Button("Cancel", event -> dialog.close()))));
        dialog.open();
    }

    private void showAccount() {
        content.removeAll();
        content.add(new H3("Account"));
        content.add(new Button("Back to expenses", event -> showWorkspaceContent(currentSection)));
        RealUserEntity user = currentUser();
        TextField username = new TextField("Username");
        username.setValue(user.getName());
        username.setReadOnly(true);
        TextField email = new TextField("Email");
        email.setValue(Objects.requireNonNullElse(user.getEmail(), ""));
        Button saveEmail = new Button("Save email", event -> {
            user.setEmail(email.getValue().trim());
            realUserService.merge(user, false);
            success("Email updated.");
        });
        PasswordField oldPassword = new PasswordField("Current password");
        PasswordField newPassword = new PasswordField("New password");
        PasswordField confirmPassword = new PasswordField("Confirm new password");
        Button savePassword = new Button("Change password", event -> {
            if (!Objects.equals(oldPassword.getValue(), user.getClearPassword())) {
                error("The current password is incorrect.");
                return;
            }
            if (newPassword.getValue().isBlank() || !newPassword.getValue().equals(confirmPassword.getValue())) {
                error("Enter matching new passwords.");
                return;
            }
            user.setClearPassword(newPassword.getValue());
            realUserService.merge(user, true);
            oldPassword.clear();
            newPassword.clear();
            confirmPassword.clear();
            success("Password updated. Existing sheet encryption keys are unchanged.");
        });
        content.add(new VerticalLayout(username, email, saveEmail, new H4("Change password"),
                oldPassword, newPassword, confirmPassword, savePassword));
    }

    private void refreshSection() {
        switch (currentSection) {
            case "Month" -> showMonth();
            case "Expenses" -> showTable();
            case "Charts" -> showCharts();
            case "User summaries" -> showSummaries();
            case "Settings" -> showSettings();
            default -> showDay();
        }
    }

    private BigDecimal sum(List<ExpenseEntity> expenses) {
        return expenses.stream().map(ExpenseEntity::getValue).filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void error(String message) {
        Notification notification = Notification.show(message, 5000, Notification.Position.TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
    }

    private void success(String message) {
        Notification notification = Notification.show(message, 3500, Notification.Position.TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    private record UserSummaryRow(String user, UserSummaryEntity summary) {
    }

    private record CategoryTotal(String name, BigDecimal total) {
    }

    private record UserBudget(String name, BigDecimal spent, BigDecimal remaining) {
    }
}
