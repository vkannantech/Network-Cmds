# Run HTTP Client (PowerShell)
cd $PSScriptRoot
if (!(Test-Path Clienthttp.class)) { javac Clienthttp.java }
Write-Host "Running Clienthttp (Connecting to localhost:387)..." -ForegroundColor Cyan
java Clienthttp
Read-Host "Press Enter to exit"
