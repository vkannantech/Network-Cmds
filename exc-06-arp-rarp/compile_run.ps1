# Compile and Run ARP/RARP (PowerShell)
cd $PSScriptRoot
Write-Host "Compiling ARPRARP.java..." -ForegroundColor Cyan
javac ARPRARP.java
if ($LASTEXITCODE -eq 0) {
    Write-Host "Running ARPRARP..." -ForegroundColor Green
    java ARPRARP
} else {
    Write-Host "[ERROR] Compilation failed!" -ForegroundColor Red
}
Read-Host "Press Enter to exit"
