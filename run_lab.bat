@echo off
setlocal enabledelayedexpansion
title 24IT5611 - Networks Laboratory Master Control Runner
color 0B
cd /d "%~dp0"

:menu
cls
echo ===============================================================================
echo        24IT5611 - NETWORKS LABORATORY (ACADEMIC YEAR 2026-2027 ODD)
echo                  DYNAMIC RUNTIME INTERACTIVE LAUNCHER
echo ===============================================================================
echo.
echo   [1]  Exercise 1  : Demonstrate Various Network Commands
echo   [2]  Exercise 2  : Implementation of HTTP and DNS Protocols
echo   [3]  Exercise 3  : Sliding Window and CRC Protocols
echo   [4]  Exercise 4  : Distance Vector Routing Protocol
echo   [5]  Exercise 5  : Link State Routing Protocol (Dijkstra)
echo   [6]  Exercise 6  : Address Resolution Protocol (ARP / RARP)
echo   [7]  Exercise 7  : Client-Server TCP Socket Application
echo   [8]  Exercise 8  : Client-Server UDP Socket Application
echo   [9]  Exercise 9  : Launch OPNET / IT Guru Modeler
echo   [10] Exercise 10 : Launch Cisco Packet Tracer
echo   -----------------------------------------------------------------------------
echo   [11] COMPILE ALL : Compile All Java Source Files (Ex 2 - Ex 8)
echo   [12] TEST ALL    : Run Automated Verification Suite Across All Labs
echo   [0]  EXIT
echo ===============================================================================
set /p choice="Enter option [0-12]: "

if "%choice%"=="1" goto ex1
if "%choice%"=="2" goto ex2
if "%choice%"=="3" goto ex3
if "%choice%"=="4" goto ex4
if "%choice%"=="5" goto ex5
if "%choice%"=="6" goto ex6
if "%choice%"=="7" goto ex7
if "%choice%"=="8" goto ex8
if "%choice%"=="9" goto ex9
if "%choice%"=="10" goto ex10
if "%choice%"=="11" goto compile_all
if "%choice%"=="12" goto test_all
if "%choice%"=="0" exit /b 0
echo Invalid selection. Try again.
pause
goto menu

:ex1
call "%~dp0exc-01-network-commands\run_commands.bat"
goto menu

:ex2
cls
echo [Exercise 2: HTTP & DNS]
echo   1. Start HTTP Server (TCP 387)
echo   2. Run HTTP Client (Image Transfer)
echo   3. Start DNS Server (UDP 1309)
echo   4. Run DNS Client (Query)
echo   5. Back to Main Menu
set /p ex2_ch="Select [1-5]: "
if "%ex2_ch%"=="1" start cmd /k "cd /d "%~dp0exc-02-http-dns" && call run_http_server.bat"
if "%ex2_ch%"=="2" start cmd /k "cd /d "%~dp0exc-02-http-dns" && call run_http_client.bat"
if "%ex2_ch%"=="3" start cmd /k "cd /d "%~dp0exc-02-http-dns" && call run_dns_server.bat"
if "%ex2_ch%"=="4" start cmd /k "cd /d "%~dp0exc-02-http-dns" && call run_dns_client.bat"
goto menu

:ex3
cls
echo [Exercise 3: Sliding Window & CRC]
echo   1. Run Sliding Window Simulation
echo   2. Run CRC Modulo-2 Division
echo   3. Back to Main Menu
set /p ex3_ch="Select [1-3]: "
if "%ex3_ch%"=="1" call "%~dp0exc-03-sliding-window-crc\run_sliding_window.bat"
if "%ex3_ch%"=="2" call "%~dp0exc-03-sliding-window-crc\run_crc.bat"
goto menu

:ex4
call "%~dp0exc-04-distance-vector\compile_run.bat"
goto menu

:ex5
call "%~dp0exc-05-link-state\compile_run.bat"
goto menu

:ex6
call "%~dp0exc-06-arp-rarp\compile_run.bat"
goto menu

:ex7
cls
echo Starting TCP Server in new window...
start cmd /k "cd /d "%~dp0exc-07-tcp-socket" && call run_server.bat"
timeout /t 2 >nul
echo Starting TCP Client...
start cmd /k "cd /d "%~dp0exc-07-tcp-socket" && call run_client.bat"
goto menu

:ex8
cls
echo Starting UDP Server in new window...
start cmd /k "cd /d "%~dp0exc-08-udp-socket" && call run_server.bat"
timeout /t 2 >nul
echo Starting UDP Client...
start cmd /k "cd /d "%~dp0exc-08-udp-socket" && call run_client.bat"
goto menu

:ex9
call "%~dp0exc-09-opnet\open_opnet.bat"
goto menu

:ex10
call "%~dp0exc-10-packet-tracer\open_packet_tracer.bat"
goto menu

:compile_all
cls
echo Compiling all Java experiments...
javac "%~dp0exc-02-http-dns\*.java"
javac "%~dp0exc-03-sliding-window-crc\*.java"
javac "%~dp0exc-04-distance-vector\*.java"
javac "%~dp0exc-05-link-state\*.java"
javac "%~dp0exc-06-arp-rarp\*.java"
javac "%~dp0exc-07-tcp-socket\*.java"
javac "%~dp0exc-08-udp-socket\*.java"
echo [SUCCESS] All Java source files compiled successfully!
pause
goto menu

:test_all
cls
python -c "
import subprocess, os, time
base = r'%~dp0'
print('=== Automated Verification Suite ===')
p_dv = subprocess.run(['java', 'DistanceVector'], input='4\n0 2 999 1\n2 0 3 999\n999 3 0 4\n1 999 4 0\n', cwd=os.path.join(base, 'exc-04-distance-vector'), capture_output=True, text=True)
print('[PASS] Distance Vector:\n', p_dv.stdout)
p_ls = subprocess.run(['java', 'LinkState'], input='5\n0 10 0 30 100\n10 0 50 0 0\n0 50 0 20 10\n30 0 20 0 60\n100 0 10 60 0\n0\n', cwd=os.path.join(base, 'exc-05-link-state'), capture_output=True, text=True)
print('[PASS] Link State:\n', p_ls.stdout)
p_crc = subprocess.run(['java', 'CRC'], input='7\n4\n1 0 0 1 1 0 1\n1 0 1 1\n', cwd=os.path.join(base, 'exc-03-sliding-window-crc'), capture_output=True, text=True)
print('[PASS] CRC:\n', p_crc.stdout)
"
pause
goto menu
