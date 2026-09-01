# Compile and Run Distance Vector (PowerShell)
cd $PSScriptRoot
Write-Host "Compiling DistanceVector.java..." -ForegroundColor Cyan
javac DistanceVector.java
if ($LASTEXITCODE -eq 0) {
    Write-Host "Running DistanceVector..." -ForegroundColor Green
    java DistanceVector
} else {
    Write-Host "[ERROR] Compilation failed!" -ForegroundColor Red
}
Read-Host "Press Enter to exit"
