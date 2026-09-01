@echo off
setlocal
title ARP & RARP Protocol Simulation - 24IT5611
cd /d "%~dp0"
javac ARPRARP.java
if %errorlevel% neq 0 (echo [ERROR] Compilation failed. & pause & exit /b 1)
echo Running ARPRARP...
java ARPRARP
pause
