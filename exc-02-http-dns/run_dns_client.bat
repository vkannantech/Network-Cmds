@echo off
setlocal
title DNS Client (UDP Port 1309) - 24IT5611
cd /d "%~dp0"
if not exist Clientdns12.class javac Clientdns12.java
echo Starting DNS Client (Connecting to localhost:1309)...
java Clientdns12
pause
