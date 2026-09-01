@echo off
setlocal
title CRC Protocol - 24IT5611
cd /d "%~dp0"
if not exist CRC.class javac CRC.java
echo Running CRC (Sample: data bits=7, gen bits=4)...
java CRC
pause
