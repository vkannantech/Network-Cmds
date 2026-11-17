# Run TCP Client (PowerShell)
cd $PSScriptRoot
if (!(Test-Path Client.class)) { javac Client.java }
Write-Host "Starting TCP Client (Connecting to localhost:5000)..." -ForegroundColor Cyan
java Client
Read-Host "Press Enter to exit"
