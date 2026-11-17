# Run Sliding Window (PowerShell)
cd $PSScriptRoot
if (!(Test-Path SlidingWindow.class)) { javac SlidingWindow.java }
Write-Host "Running SlidingWindow (Sample: frames=8, window=4)..." -ForegroundColor Cyan
java SlidingWindow
Read-Host "Press Enter to exit"
