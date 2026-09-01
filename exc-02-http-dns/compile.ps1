# Compile Exercise 2 (PowerShell)
cd $PSScriptRoot
Write-Host "Compiling Exercise 2 Java files..." -ForegroundColor Cyan
javac Clienthttp.java Serverhttp.java Clientdns12.java Serverdns12.java
if ($LASTEXITCODE -eq 0) { Write-Host "[SUCCESS] Compiled successfully!" -ForegroundColor Green }
else { Write-Host "[ERROR] Compilation failed." -ForegroundColor Red }
Read-Host "Press Enter to exit"
