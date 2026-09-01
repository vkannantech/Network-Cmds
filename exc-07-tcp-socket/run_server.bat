@echo off
setlocal
title TCP Server (Port 5000) - 24IT5611
cd /d "%~dp0"
if not exist Server.class javac Server.java
echo Starting TCP Server on port 5000...
java Server
pause
