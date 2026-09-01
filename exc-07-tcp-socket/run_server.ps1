# Run TCP Server (PowerShell)
cd $PSScriptRoot
if (!(Test-Path Server.class)) { javac Server.java }
Write-Host "Starting TCP Server on port 5000..." -ForegroundColor Green
java Server
Read-Host "Press Enter to exit"
