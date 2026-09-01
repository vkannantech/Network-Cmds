@echo off
setlocal enabledelayedexpansion
title Cisco Packet Tracer Launcher - 24IT5611
cd /d "%~dp0"

if defined PACKET_TRACER_EXE (
    if exist "!PACKET_TRACER_EXE!" (
        echo Launching Cisco Packet Tracer: "!PACKET_TRACER_EXE!"
        start "" "!PACKET_TRACER_EXE!"
        pause & exit /b 0
    )
)

set "P0=C:\Program Files\Cisco Packet Tracer 8.2.2in\PacketTracer.exe"
set "P1=C:\Program Files\Cisco Packet Tracer 8.2.1in\PacketTracer.exe"
set "P2=C:\Program Files\Cisco Packet Tracer 8.2.0in\PacketTracer.exe"
set "P3=C:\Program Files\Cisco Packet Tracer 8.1.1in\PacketTracer.exe"
set "P4=C:\Program Files\Cisco Packet Tracer 8.0in\PacketTracer.exe"
set "P5=C:\Program Files\Cisco Packet Tracer 7.3.1in\PacketTracer.exe"

for %%p in ("%P0%" "%P1%" "%P2%" "%P3%" "%P4%" "%P5%") do (
    if exist %%p (
        echo Launching Cisco Packet Tracer: %%p
        start "" %%p
        pause & exit /b 0
    )
)

echo [ENVIRONMENT NOTICE] Cisco Packet Tracer was not found in default paths.
echo Please configure PACKET_TRACER_EXE: set PACKET_TRACER_EXE=C:\Path\To\PacketTracer.exe
echo Refer to project-checklist.md and README.md for configuration instructions.
pause
