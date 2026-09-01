@echo off
setlocal
title HTTP Server (Port 387) - 24IT5611
cd /d "%~dp0"
if not exist Serverhttp.class javac Serverhttp.java
echo Starting Serverhttp on TCP port 387...
java Serverhttp
pause
