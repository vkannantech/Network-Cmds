@echo off
REM Windows: compiles then runs WebServer. Extra arguments are passed to the program.
cd /d "%~dp0"
javac *.java
if errorlevel 1 goto end
java WebServer %*
:end
pause
