#!/usr/bin/env bash
# Q1 - Layer-by-layer network fault isolation (Linux)
# Usage: ./diagnose.sh [hostname-to-test]      default: www.google.com
TARGET=${1:-www.google.com}
PUBLIC_IP=8.8.8.8
pass(){ echo "   [PASS] $1"; }
fail(){ echo "   [FAIL] $1"; echo; echo ">>> DIAGNOSIS: $2"; exit 1; }
line(){ echo; echo "=============================================================="; echo " STEP $1"; echo "=============================================================="; }

line "1 : IP configuration  (ifconfig / ip addr)"
command -v ifconfig >/dev/null && ifconfig | grep -E "^[a-z]|inet " || ip addr
MYIP=$(ip -4 route get $PUBLIC_IP 2>/dev/null | grep -oP 'src \K[\d.]+')
[ -n "$MYIP" ] && pass "Host has IPv4 address $MYIP" || fail "No IPv4 address / no route" \
 "IP CONFIGURATION problem - interface down or DHCP failed. Fix: check cable/Wi-Fi, run 'sudo dhclient' or set static IP."
case $MYIP in 169.254.*) fail "APIPA address $MYIP" "DHCP server not reachable - IP CONFIGURATION problem.";; esac

line "2 : Loopback test  (ping 127.0.0.1) - is TCP/IP stack working?"
ping -c 2 -W 1 127.0.0.1 >/dev/null && pass "TCP/IP stack OK" || fail "Loopback failed" "TCP/IP stack broken - reinstall/restart networking."

line "3 : Default gateway  (ip route + ping gateway)"
GW=$(ip route | awk '/^default/{print $3; exit}')
echo "   Default gateway = ${GW:-NONE}"
[ -z "$GW" ] && fail "No default gateway" "IP CONFIGURATION problem - default gateway missing."
ping -c 2 -W 1 $GW >/dev/null && pass "Gateway $GW reachable" || fail "Gateway $GW unreachable" \
 "LOCAL LAN problem - cable / switch / wrong gateway / wrong subnet mask."

line "4 : Internet by IP  (ping $PUBLIC_IP) - bypasses DNS"
ping -c 3 -W 2 $PUBLIC_IP && pass "Remote network reachable by IP" || {
  echo; echo "   Tracing where packets stop:"; (traceroute -n -m 10 $PUBLIC_IP 2>/dev/null || tracepath -n -m 10 $PUBLIC_IP)
  fail "Cannot reach $PUBLIC_IP" "ROUTING problem - look at the LAST hop shown by traceroute: the fault is just after it."; }

line "5 : DNS resolution  (nslookup $TARGET)"
grep nameserver /etc/resolv.conf
nslookup $TARGET && pass "DNS resolves $TARGET" || fail "nslookup failed" \
 "DNS FAILURE - ping by IP works but names do not resolve. Fix DNS server in /etc/resolv.conf or DHCP (e.g. 8.8.8.8)."

line "6 : Path to target  (traceroute / tracepath $TARGET)"
traceroute -m 15 $TARGET 2>/dev/null || tracepath -m 15 $TARGET

line "7 : Connections & listening services  (netstat)"
(netstat -tuln 2>/dev/null || ss -tuln) | head -15
echo "   Routing table (netstat -rn):"; (netstat -rn 2>/dev/null || ip route)

echo; echo ">>> DIAGNOSIS: All layers OK - IP config, gateway, routing and DNS are working."
echo "    If an internal service still fails, check it is LISTENING in the netstat output above (service/firewall problem)."
