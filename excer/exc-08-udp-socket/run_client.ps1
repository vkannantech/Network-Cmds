# Run UDP Client (PowerShell)
cd $PSScriptRoot
if (!(Test-Path UDPClient.class)) { javac UDPClient.java }
Write-Host "Starting UDP Client (Connecting to localhost:5000)..." -ForegroundColor Cyan
java UDPClient
Read-Host "Press Enter to exit"
