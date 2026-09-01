# Run HTTP Server (PowerShell)
cd $PSScriptRoot
if (!(Test-Path Serverhttp.class)) { javac Serverhttp.java }
Write-Host "Starting HTTP Server on TCP port 387..." -ForegroundColor Green
java Serverhttp
Read-Host "Press Enter to exit"
