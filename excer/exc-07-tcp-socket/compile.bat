@echo off
setlocal
title Compile Exercise 7 - TCP Socket
cd /d "%~dp0"
javac Server.java Client.java
if %errorlevel% equ 0 (echo [SUCCESS] Compilation completed successfully!) else (echo [ERROR] Compilation failed.)
pause
