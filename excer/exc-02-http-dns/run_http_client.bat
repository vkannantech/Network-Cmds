@echo off

setlocal

title HTTP Client (Image Sender) - Networks-Commands-Deeply

cd /d "%~dp0"

if not exist Clienthttp.class javac Clienthttp.java

echo Running Clienthttp (Connecting to localhost:387)...

java Clienthttp

pause

