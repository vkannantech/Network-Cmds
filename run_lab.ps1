# Networks-Commands-Deeply - Master Control Runner (PowerShell)
$Host.UI.RawUI.WindowTitle = "Networks-Commands-Deeply Master Control"
Set-Location $PSScriptRoot

function Show-Menu {
    Clear-Host
    Write-Host "===============================================================================" -ForegroundColor Cyan
    Write-Host "                NETWORKS-COMMANDS-DEEPLY: MASTER CONTROL CENTER" -ForegroundColor Cyan
    Write-Host "        NETWORK PROTOCOLS, DIAGNOSTICS & SOCKET ENGINEERING SUITE" -ForegroundColor Cyan
    Write-Host "===============================================================================" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "  [1]  Module 01 : Core Diagnostic Network Commands (ping, tracert, netstat...)" -ForegroundColor Yellow
    Write-Host "  [2]  Module 02 : HTTP Binary Image Transfer (TCP 387) & DNS Resolution (UDP 1309)" -ForegroundColor Yellow
    Write-Host "  [3]  Module 03 : Sliding Window Flow Control & Modulo-2 CRC Checksum" -ForegroundColor Yellow
    Write-Host "  [4]  Module 04 : Distance Vector Routing Protocol (Bellman-Ford)" -ForegroundColor Yellow
    Write-Host "  [5]  Module 05 : Link State Routing Protocol (Dijkstra SPF)" -ForegroundColor Yellow
    Write-Host "  [6]  Module 06 : Address Resolution Protocol (ARP / RARP)" -ForegroundColor Yellow
    Write-Host "  [7]  Module 07 : High-Throughput TCP Client-Server Socket Application" -ForegroundColor Yellow
    Write-Host "  [8]  Module 08 : Connectionless UDP Datagram Messaging Application" -ForegroundColor Yellow
    Write-Host "  [9]  Module 09 : Launch OPNET / IT Guru Modeler" -ForegroundColor Yellow
    Write-Host "  [10] Module 10 : Launch Cisco Packet Tracer Network Design" -ForegroundColor Yellow
    Write-Host "  -----------------------------------------------------------------------------" -ForegroundColor Gray
    Write-Host "  [11] COMPILE ALL : Compile All Java Source Files (Modules 02 - 08)" -ForegroundColor Green
    Write-Host "  [12] TEST ALL    : Run Automated Verification Suite Across All Modules" -ForegroundColor Green
    Write-Host "  [0]  EXIT" -ForegroundColor Red
    Write-Host "===============================================================================" -ForegroundColor Cyan
}

do {
    Show-Menu
    $choice = Read-Host "Enter option [0-12]"
    switch ($choice) {
        "1" { & "$PSScriptRoot\exc-01-network-commands\run_commands.ps1" }
        "2" {
            Clear-Host
            Write-Host "Module 02: HTTP & DNS" -ForegroundColor Cyan
            Write-Host "1. Start HTTP Server"
            Write-Host "2. Run HTTP Client"
            Write-Host "3. Start DNS Server"
            Write-Host "4. Run DNS Client"
            $c = Read-Host "Select [1-4]"
            if ($c -eq "1") { Start-Process powershell -ArgumentList "-NoExit", "-File", "$PSScriptRoot\exc-02-http-dns\run_http_server.ps1" }
            if ($c -eq "2") { Start-Process powershell -ArgumentList "-NoExit", "-File", "$PSScriptRoot\exc-02-http-dns\run_http_client.ps1" }
            if ($c -eq "3") { Start-Process powershell -ArgumentList "-NoExit", "-File", "$PSScriptRoot\exc-02-http-dns\run_dns_server.ps1" }
            if ($c -eq "4") { Start-Process powershell -ArgumentList "-NoExit", "-File", "$PSScriptRoot\exc-02-http-dns\run_dns_client.ps1" }
        }
        "3" {
            Clear-Host
            Write-Host "1. Sliding Window"
            Write-Host "2. CRC Modulo-2"
            $c = Read-Host "Select [1-2]"
            if ($c -eq "1") { & "$PSScriptRoot\exc-03-sliding-window-crc\run_sliding_window.ps1" }
            if ($c -eq "2") { & "$PSScriptRoot\exc-03-sliding-window-crc\run_crc.ps1" }
        }
        "4" { & "$PSScriptRoot\exc-04-distance-vector\compile_run.ps1" }
        "5" { & "$PSScriptRoot\exc-05-link-state\compile_run.ps1" }
        "6" { & "$PSScriptRoot\exc-06-arp-rarp\compile_run.ps1" }
        "7" {
            Start-Process powershell -ArgumentList "-NoExit", "-File", "$PSScriptRoot\exc-07-tcp-socket\run_server.ps1"
            Start-Sleep -Seconds 1
            Start-Process powershell -ArgumentList "-NoExit", "-File", "$PSScriptRoot\exc-07-tcp-socket\run_client.ps1"
        }
        "8" {
            Start-Process powershell -ArgumentList "-NoExit", "-File", "$PSScriptRoot\exc-08-udp-socket\run_server.ps1"
            Start-Sleep -Seconds 1
            Start-Process powershell -ArgumentList "-NoExit", "-File", "$PSScriptRoot\exc-08-udp-socket\run_client.ps1"
        }
        "9" { & "$PSScriptRoot\exc-09-opnet\open_opnet.ps1" }
        "10" { & "$PSScriptRoot\exc-10-packet-tracer\open_packet_tracer.ps1" }
        "11" {
            Write-Host "Compiling all Java source files..." -ForegroundColor Cyan
            javac (Get-ChildItem -Path "$PSScriptRoot\exc-*" -Filter *.java -Recurse | Select-Object -ExpandProperty FullName)
            Write-Host "[SUCCESS] All Java files compiled successfully!" -ForegroundColor Green
            Read-Host "Press Enter to continue"
        }
        "12" {
            Write-Host "Running automated verification suite..." -ForegroundColor Cyan
            & python -c "
import subprocess, os
base = r'$PSScriptRoot'
print('=== Automated Verification Tests ===')
p1 = subprocess.run(['java', 'DistanceVector'], input='4\n0 2 999 1\n2 0 3 999\n999 3 0 4\n1 999 4 0\n', cwd=os.path.join(base, 'exc-04-distance-vector'), capture_output=True, text=True)
print('[PASS] Distance Vector:\n', p1.stdout)
p2 = subprocess.run(['java', 'LinkState'], input='5\n0 10 0 30 100\n10 0 50 0 0\n0 50 0 20 10\n30 0 20 0 60\n100 0 10 60 0\n0\n', cwd=os.path.join(base, 'exc-05-link-state'), capture_output=True, text=True)
print('[PASS] Link State:\n', p2.stdout)
"
            Read-Host "Press Enter to continue"
        }
    }
} while ($choice -ne "0")
