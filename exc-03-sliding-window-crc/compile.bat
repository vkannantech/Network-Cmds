@echo off
setlocal
title Compile Exercise 3 - Sliding Window & CRC
cd /d "%~dp0"
javac SlidingWindow.java CRC.java
if %errorlevel% equ 0 (echo [SUCCESS] Compilation completed successfully!) else (echo [ERROR] Compilation failed.)
pause
