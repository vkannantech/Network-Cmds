# Run UDP Server (PowerShell)
cd $PSScriptRoot
if (!(Test-Path UDPServer.class)) { javac UDPServer.java }
Write-Host "Starting UDP Server on port 5000..." -ForegroundColor Green
java UDPServer
Read-Host "Press Enter to exit"
