@echo off
setlocal
title UDP Client (Port 5000) - 24IT5611
cd /d "%~dp0"
if not exist UDPClient.class javac UDPClient.java
echo Starting UDP Client (Connecting to localhost:5000)...
java UDPClient
pause
