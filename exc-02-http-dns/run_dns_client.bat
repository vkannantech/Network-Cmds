@echo off

setlocal

title DNS Client (UDP Port 1309) - Networks-Commands-Deeply

cd /d "%~dp0"

if not exist Clientdns12.class javac Clientdns12.java

echo Starting DNS Client (Connecting to localhost:1309)...

java Clientdns12

pause

