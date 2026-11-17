# OPNET Launcher (PowerShell)
if ($env:OPNET_EXE -and (Test-Path $env:OPNET_EXE)) {
    Start-Process $env:OPNET_EXE
    exit
}
$paths = @(
    "C:\OPNET.5.A\models\stdin\opnet.exe",
    "C:\Program Files\OPNET.5.A\models\stdin\opnet.exe",
    "C:\Program Files (x86)\OPNET.5.A\models\stdin\opnet.exe",
    "C:\OPNET_IT_Guruin\opnet.exe",
    "C:\Riverbed\Modelerin\modeler.exe"
)
foreach ($p in $paths) {
    if (Test-Path $p) {
        Start-Process $p
        exit
    }
}
Write-Host "[ENVIRONMENT NOTICE] OPNET / IT Guru was not found." -ForegroundColor Yellow
Write-Host "Set `$env:OPNET_EXE='C:\Path\To\opnet.exe' and rerun." -ForegroundColor Cyan
Read-Host "Press Enter to exit"
