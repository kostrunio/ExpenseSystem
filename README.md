# ExpenseSystem — Vaadin Flow 25

ExpenseSystem has been migrated from Vaadin 8 to Vaadin Flow 25. The application
retains the existing Jakarta-compatible JPA model, PostgreSQL schema, business
services, scheduled notifications, and encrypted expense-sheet data.

The Flow interface includes:

- Account sign-in, registration, email/password updates, and sign-out
- Expense sheet selection, creation, sharing/access removal, unlocking, password rotation, and deletion
- Daily expense entry and editing, monthly calendar replacement, and category/user totals
- Filterable expense table with CSV export
- Filterable monthly category charts built with standard Flow components
- User spending summaries, history editing, and budget balances
- Settings for categories, ordering, multipliers, users, limits, and continuous summaries

The Vaadin 8 Charts, Calendar, and Exporter add-ons were replaced with Flow-native
views and a CSV download. The month view lists each day instead of rendering a
third-party calendar grid.

## Build and run

Requires Java 21 and Maven. Run `mvn clean verify` to build the Vaadin frontend,
execute the in-memory Spring/HTTP smoke test, and package the WAR. Configure
`JDBC_DATABASE_URL` and, when needed, `SPRING_DATASOURCE_USERNAME` and
`SPRING_DATASOURCE_PASSWORD` for the existing PostgreSQL database before running
`mvn spring-boot:run`.
