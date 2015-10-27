timeout 60
C:\"Program Files"\PostgreSQL\9.4\bin\pg_dump.exe -U postgres -C -f C:\Users\magdalenka\git\ExpenseSystem\export\%date%.dmp postgres
if %ERRORLEVEL% EQU 0 (
  xcopy C:\Users\magdalenka\git\ExpenseSystem\export \\dns\sklad\backup\expenseSystem /D /Y
)