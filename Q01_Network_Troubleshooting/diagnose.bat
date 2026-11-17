@echo off
REM Q1 - Layer-by-layer network fault isolation (Windows)
set TARGET=%1
if "%TARGET%"=="" set TARGET=www.google.com
echo ===== STEP 1: IP configuration (ipconfig /all) =====
ipconfig /all
echo ===== STEP 2: Loopback (ping 127.0.0.1) =====
ping -n 2 127.0.0.1
echo ===== STEP 3: Default gateway (route print / ping gateway) =====
for /f "tokens=3" %%g in ('route print 0.0.0.0 ^| findstr /r "^ *0.0.0.0"') do set GW=%%g
echo Default gateway = %GW%
ping -n 2 %GW%
echo ===== STEP 4: Internet by IP (ping 8.8.8.8) - bypasses DNS =====
ping -n 3 8.8.8.8
echo ===== STEP 5: DNS (nslookup %TARGET%) =====
nslookup %TARGET%
echo ===== STEP 6: Route (tracert %TARGET%) =====
tracert -h 15 %TARGET%
echo ===== STEP 7: Connections (netstat -an / netstat -r) =====
netstat -an | findstr LISTENING
netstat -r
echo.
echo Read the results top-down: the FIRST step that fails is the faulty layer.
pause
