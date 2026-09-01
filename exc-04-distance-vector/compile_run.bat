@echo off

setlocal

title Distance Vector Routing - Networks-Commands-Deeply

cd /d "%~dp0"

javac DistanceVector.java

if %errorlevel% neq 0 (echo [ERROR] Compilation failed. & pause & exit /b 1)

echo Running DistanceVector (Sample: 4 nodes, 4x4 matrix)...

java DistanceVector

pause

