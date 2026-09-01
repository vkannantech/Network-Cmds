@echo off

setlocal

title Link State Routing (Dijkstra) - Networks-Commands-Deeply

cd /d "%~dp0"

javac LinkState.java

if %errorlevel% neq 0 (echo [ERROR] Compilation failed. & pause & exit /b 1)

echo Running LinkState (Sample: 5 nodes, source=0)...

java LinkState

pause

