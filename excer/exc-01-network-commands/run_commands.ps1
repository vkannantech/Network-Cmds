# Networks-Commands-Deeply - Exercise 1: Network Commands Demonstration (PowerShell)

$Host.UI.RawUI.WindowTitle = "Networks-Commands-Deeply - Exercise 1: Network Commands"



Write-Host "===============================================================================" -ForegroundColor Cyan

Write-Host "       Networks-Commands-Deeply - NETWORKS LABORATORY - EXERCISE 1" -ForegroundColor Cyan

Write-Host "       DEMONSTRATE VARIOUS NETWORK COMMANDS" -ForegroundColor Cyan

Write-Host "===============================================================================" -ForegroundColor Cyan

Write-Host ""



Write-Host "[1/10] Testing Reachability with PING (localhost and 8.8.8.8)..." -ForegroundColor Yellow

ping -n 2 127.0.0.1

ping -n 2 8.8.8.8

Write-Host ""



Write-Host "[2/10] Inspecting Network Statistics with NETSTAT..." -ForegroundColor Yellow

netstat -an | Select-String -Pattern "LISTENING|ESTABLISHED" | Select-Object -First 15

Write-Host ""



Write-Host "[3/10] Viewing Network Configuration with IPCONFIG..." -ForegroundColor Yellow

ipconfig /all

Write-Host ""



Write-Host "[4/10] Querying DNS Infrastructure with NSLOOKUP..." -ForegroundColor Yellow

nslookup google.com

nslookup 8.8.8.8

Write-Host ""



Write-Host "[5/10] Tracing Packet Route with TRACERT..." -ForegroundColor Yellow

tracert -d -h 5 8.8.8.8

Write-Host ""



Write-Host "[6/10] Inspecting Address Resolution Protocol Table with ARP..." -ForegroundColor Yellow

arp -a

Write-Host ""



Write-Host "[7/10] Getting Host Name with HOSTNAME..." -ForegroundColor Yellow

hostname

Write-Host ""



Write-Host "[8/10] Displaying System Configuration with SYSTEMINFO..." -ForegroundColor Yellow

systeminfo | Select-String -Pattern "OS Name|OS Version|System Type|Total Physical Memory|Domain"

Write-Host ""



Write-Host "[9/10] Displaying Physical MAC Addresses with GETMAC..." -ForegroundColor Yellow

getmac /v

Write-Host ""



Write-Host "[10/10] Measuring Latency and Packet Loss with PATHPING..." -ForegroundColor Yellow

pathping -n -q 1 -h 3 8.8.8.8

Write-Host ""



Write-Host "===============================================================================" -ForegroundColor Green

Write-Host "Network commands demonstration completed successfully." -ForegroundColor Green

Write-Host "===============================================================================" -ForegroundColor Green

Read-Host "Press Enter to exit"

