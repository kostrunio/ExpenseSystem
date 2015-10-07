timeout 60
expdp expenseSystem/expenseSystem SCHEMAS=expenseSystem DIRECTORY=exp_dir  DUMPFILE=%date%.dmp LOGFILE=expschema.log
if %ERRORLEVEL% EQU 0 (
  xcopy C:\Users\magdalenka\workspace\ExpenseSystem\export \\dns\sklad\backup\expenseSystem /D /Y
)