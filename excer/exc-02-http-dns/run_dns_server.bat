@echo off

setlocal

title DNS Server (UDP Port 1309) - Networks-Commands-Deeply

cd /d "%~dp0"

if not exist Serverdns12.class javac Serverdns12.java

echo Starting DNS Server on UDP Port 1309...

java Serverdns12

pause

