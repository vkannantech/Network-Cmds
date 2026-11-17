@echo off

setlocal

title Sliding Window Protocol - Networks-Commands-Deeply

cd /d "%~dp0"

if not exist SlidingWindow.class javac SlidingWindow.java

echo Running SlidingWindow (Sample: frames=8, window=4)...

java SlidingWindow

pause

