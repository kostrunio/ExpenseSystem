# ExpenseSystem — Vaadin Flow 25 migration foundation

This branch ports the ExpenseSystem domain model, JPA repositories, business services,
and shared utilities from Vaadin 8 to the Jakarta APIs used by Vaadin Flow 25. It
retains Spring Boot, PostgreSQL, scheduled notifications, and the existing schema.

The old Vaadin 8 UI depended on Navigator, generated Design classes, custom GWT
widgetsets, Charts, Calendar, and Exporter add-ons. Those views and add-ons have not
been migrated. The root route is a safe migration-status page and does not expose
expense data while authentication and authorization are absent.

## Not yet ported

- Login, registration, and account management
- Expense-sheet creation and password management
- Daily/monthly expense entry and calendar navigation
- Expense tables, charts, filters, and user summaries
- Settings for categories, users, and spending limits
- Vaadin 8 Charts, Calendar, and Exporter integrations

## Build and run

Requires Java 21 and Maven. Run `mvn test` to compile the application and execute
tests. For local startup, configure `JDBC_DATABASE_URL` for the existing PostgreSQL
database, and optionally `SPRING_DATASOURCE_USERNAME` and
`SPRING_DATASOURCE_PASSWORD`, then run `mvn spring-boot:run`.
