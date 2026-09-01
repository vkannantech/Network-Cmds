# Compile and Run Link State (PowerShell)
cd $PSScriptRoot
Write-Host "Compiling LinkState.java..." -ForegroundColor Cyan
javac LinkState.java
if ($LASTEXITCODE -eq 0) {
    Write-Host "Running LinkState..." -ForegroundColor Green
    java LinkState
} else {
    Write-Host "[ERROR] Compilation failed!" -ForegroundColor Red
}
Read-Host "Press Enter to exit"
