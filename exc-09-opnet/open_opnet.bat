@echo off
setlocal enabledelayedexpansion
title OPNET Launcher - 24IT5611
cd /d "%~dp0"

if defined OPNET_EXE (
    if exist "!OPNET_EXE!" (
        echo Launching OPNET: "!OPNET_EXE!"
        start "" "!OPNET_EXE!"
        pause & exit /b 0
    )
)

set "P0=C:\OPNET.5.A\models\stdin\opnet.exe"
set "P1=C:\Program Files\OPNET.5.A\models\stdin\opnet.exe"
set "P2=C:\Program Files (x86)\OPNET.5.A\models\stdin\opnet.exe"
set "P3=C:\OPNET_IT_Guruin\opnet.exe"
set "P4=C:\Riverbed\Modelerin\modeler.exe"

for %%p in ("%P0%" "%P1%" "%P2%" "%P3%" "%P4%") do (
    if exist %%p (
        echo Launching OPNET: %%p
        start "" %%p
        pause & exit /b 0
    )
)

echo [ENVIRONMENT NOTICE] OPNET / IT Guru was not found in standard paths.
echo Please configure OPNET_EXE: set OPNET_EXE=C:\Path\To\opnet.exe
echo Refer to project-checklist.md and README.md for simulation instructions.
pause
