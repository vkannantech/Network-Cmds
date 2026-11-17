# Cisco Packet Tracer Launcher (PowerShell)
if ($env:PACKET_TRACER_EXE -and (Test-Path $env:PACKET_TRACER_EXE)) {
    Start-Process $env:PACKET_TRACER_EXE
    exit
}
$paths = @(
    "C:\Program Files\Cisco Packet Tracer 8.2.2in\PacketTracer.exe",
    "C:\Program Files\Cisco Packet Tracer 8.2.1in\PacketTracer.exe",
    "C:\Program Files\Cisco Packet Tracer 8.2.0in\PacketTracer.exe",
    "C:\Program Files\Cisco Packet Tracer 8.1.1in\PacketTracer.exe",
    "C:\Program Files\Cisco Packet Tracer 8.0in\PacketTracer.exe"
)
foreach ($p in $paths) {
    if (Test-Path $p) {
        Start-Process $p
        exit
    }
}
Write-Host "[ENVIRONMENT NOTICE] Cisco Packet Tracer was not found." -ForegroundColor Yellow
Write-Host "Set `$env:PACKET_TRACER_EXE='C:\Path\To\PacketTracer.exe' and rerun." -ForegroundColor Cyan
Read-Host "Press Enter to exit"
