@echo off
setlocal
title Compile Exercise 2 - HTTP & DNS
cd /d "%~dp0"

echo Compiling Clienthttp.java, Serverhttp.java, Clientdns12.java, Serverdns12.java...
javac Clienthttp.java Serverhttp.java Clientdns12.java Serverdns12.java
if %errorlevel% equ 0 (echo [SUCCESS] Compiled successfully!) else (echo [ERROR] Compilation failed.)
pause
