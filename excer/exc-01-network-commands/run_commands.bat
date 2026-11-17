@echo off

setlocal enabledelayedexpansion

title Networks-Commands-Deeply - Exercise 1: Network Commands Demonstration

color 0A



echo ===============================================================================

echo        Networks-Commands-Deeply - NETWORKS LABORATORY - EXERCISE 1

echo        DEMONSTRATE VARIOUS NETWORK COMMANDS

echo ===============================================================================

echo.



echo [1/10] Testing Reachability with PING (localhost and google.com)...

echo -------------------------------------------------------------------------------

echo Pinging 127.0.0.1 (Loopback interface):

ping -n 2 127.0.0.1

echo.

echo Pinging 8.8.8.8 (Google Public DNS):

ping -n 2 8.8.8.8

echo.



echo [2/10] Inspecting Network Statistics with NETSTAT...

echo -------------------------------------------------------------------------------

echo Active TCP/UDP Connections and Listening Ports:

netstat -an | findstr /R "TCP UDP" | findstr "LISTENING ESTABLISHED"

echo.



echo [3/10] Viewing Network Configuration with IPCONFIG...

echo -------------------------------------------------------------------------------

ipconfig /all

echo.



echo [4/10] Querying DNS Infrastructure with NSLOOKUP...

echo -------------------------------------------------------------------------------

echo Resolving domain: google.com

nslookup google.com

echo.

echo Reverse lookup for 8.8.8.8:

nslookup 8.8.8.8

echo.



echo [5/10] Tracing Packet Route with TRACERT (Traceroute)...

echo -------------------------------------------------------------------------------

echo Tracing route to 8.8.8.8 (Max 5 hops for demo):

tracert -d -h 5 8.8.8.8

echo.



echo [6/10] Inspecting Address Resolution Protocol Table with ARP...

echo -------------------------------------------------------------------------------

arp -a

echo.



echo [7/10] Getting Host Name with HOSTNAME...

echo -------------------------------------------------------------------------------

hostname

echo.



echo [8/10] Displaying System Configuration with SYSTEMINFO...

echo -------------------------------------------------------------------------------

echo Summary system information:

systeminfo | findstr /B /C:"OS Name" /C:"OS Version" /C:"System Type" /C:"Total Physical Memory" /C:"Domain"

echo.



echo [9/10] Displaying Physical MAC Addresses with GETMAC...

echo -------------------------------------------------------------------------------

getmac /v

echo.



echo [10/10] Measuring Latency and Packet Loss with PATHPING...

echo -------------------------------------------------------------------------------

echo Running fast pathping (1 query per hop, max 3 hops):

pathping -n -q 1 -h 3 8.8.8.8

echo.



echo ===============================================================================

echo Network commands demonstration completed successfully.

echo ===============================================================================

pause

