@echo off
setlocal
title Compile Exercise 8 - UDP Socket
cd /d "%~dp0"
javac UDPServer.java UDPClient.java
if %errorlevel% equ 0 (echo [SUCCESS] Compilation completed successfully!) else (echo [ERROR] Compilation failed.)
pause
