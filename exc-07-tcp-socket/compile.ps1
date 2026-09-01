# Compile Exercise 7 (PowerShell)
cd $PSScriptRoot
javac Server.java Client.java
if ($LASTEXITCODE -eq 0) { Write-Host "[SUCCESS] Compiled successfully!" -ForegroundColor Green }
else { Write-Host "[ERROR] Compilation failed!" -ForegroundColor Red }
Read-Host "Press Enter to exit"
