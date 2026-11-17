@echo off

setlocal

title TCP Client (Port 5000) - Networks-Commands-Deeply

cd /d "%~dp0"

if not exist Client.class javac Client.java

echo Starting TCP Client (Connecting to localhost:5000)...

java Client

pause

