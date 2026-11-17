# Run DNS Client (PowerShell)
cd $PSScriptRoot
if (!(Test-Path Clientdns12.class)) { javac Clientdns12.java }
Write-Host "Starting DNS Client (Connecting to localhost:1309)..." -ForegroundColor Cyan
java Clientdns12
Read-Host "Press Enter to exit"
