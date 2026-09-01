@echo off
setlocal
title UDP Server (Port 5000) - 24IT5611
cd /d "%~dp0"
if not exist UDPServer.class javac UDPServer.java
echo Starting UDP Server on port 5000...
java UDPServer
pause
