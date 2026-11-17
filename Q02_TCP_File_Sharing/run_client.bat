@echo off
REM Windows: compiles then runs FileClient. Extra arguments are passed to the program.
cd /d "%~dp0"
javac *.java
if errorlevel 1 goto end
java FileClient %*
:end
pause
