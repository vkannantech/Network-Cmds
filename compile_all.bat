@echo off
REM Compile every Java program in Q01-Q12 (Windows)
cd /d "%~dp0"
for /d %%D in (Q*) do (
  if exist "%%D\*.java" (
    pushd "%%D"
    javac *.java && echo [OK]   %%D || echo [FAIL] %%D
    popd
  )
)
pause
