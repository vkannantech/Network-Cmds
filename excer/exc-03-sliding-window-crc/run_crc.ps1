# Run CRC (PowerShell)
cd $PSScriptRoot
if (!(Test-Path CRC.class)) { javac CRC.java }
Write-Host "Running CRC (Sample: data bits=7, gen bits=4)..." -ForegroundColor Cyan
java CRC
Read-Host "Press Enter to exit"
