@echo off
REM Windows: open Cisco Packet Tracer (pass a .pkt file to open it)
cd /d "%~dp0"
set PT="C:\Program Files\Cisco Packet Tracer 8.2.2\bin\PacketTracer.exe"
if not exist %PT% for /d %%D in ("C:\Program Files\Cisco Packet Tracer*") do set PT="%%D\bin\PacketTracer.exe"
if exist %PT% (start "" %PT% %*) else (echo Packet Tracer not found - open it from the Start menu)
