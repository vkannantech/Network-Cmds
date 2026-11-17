# Run DNS Server (PowerShell)
cd $PSScriptRoot
if (!(Test-Path Serverdns12.class)) { javac Serverdns12.java }
Write-Host "Starting DNS Server on UDP Port 1309..." -ForegroundColor Green
java Serverdns12
Read-Host "Press Enter to exit"
