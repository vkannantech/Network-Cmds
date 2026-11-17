#!/usr/bin/env bash
# Automatic test of all 12 questions (Linux). Usage: ./verify_all.sh
cd "$(dirname "$0")" || exit 1
ROOT=$(pwd); PASS=0; FAIL=0
T=$(mktemp -d)
check(){ if grep -q -- "$2" "$T/out"; then echo "  [PASS] $1"; PASS=$((PASS+1)); else echo "  [FAIL] $1  (expected: $2)"; FAIL=$((FAIL+1)); fi; }
server(){ (cd "$ROOT/$1" && exec java "${@:2}") > "$T/srv" 2>&1 & SP=$!; sleep 1.5; }
stop(){ kill $SP 2>/dev/null; wait $SP 2>/dev/null; }

echo "== Compile (current JDK and --release 8)"
for d in Q*/; do
  ls "$d"*.java >/dev/null 2>&1 || continue
  (cd "$d" && javac *.java) && javac --release 8 -Xlint:-options -d "$T/j8" "$d"*.java \
    && { echo "  [PASS] $d"; PASS=$((PASS+1)); } || { echo "  [FAIL] $d"; FAIL=$((FAIL+1)); }
done

echo "== Q1"; timeout 60 Q01_Network_Troubleshooting/diagnose.sh nosuchsite.invalid > $T/out 2>&1
check "detects DNS failure" "DNS FAILURE"

echo "== Q2"; server Q02_TCP_File_Sharing FileServer
(cd Q02_TCP_File_Sharing && printf 'LIST\nGET notice.txt\nGET nothere.txt\nQUIT\n' | java FileClient) > $T/out 2>&1; stop
check "GET with CRC match" "MATCHED"; check "missing file error" "File not found"
rm -rf Q02_TCP_File_Sharing/downloads

echo "== Q3"; server Q03_Reliable_UDP_Quiz QuizServer 0.3
(cd Q03_Reliable_UDP_Quiz && printf 'b\nc\nb\na\n' | timeout 120 java QuizClient localhost 0.3) > $T/out 2>&1; stop
check "full score under 30% loss" "score: 4/4"

echo "== Q4"; server Q04_HTTP_Web_Server WebServer
{ for u in / /missing.html /../../etc/passwd; do curl -s --path-as-is -o /dev/null -w "%{http_code}\n" "http://127.0.0.1:8080$u"; done
  curl -s -o /dev/null -w "%{http_code}\n" -X POST http://127.0.0.1:8080/; } > $T/out; stop
check "200" "^200"; check "404" "^404"; check "403" "^403"; check "405" "^405"

echo "== Q5"; server Q05_DNS_Resolver_UDP DNSServer
(cd Q05_DNS_Resolver_UDP && printf 'www.google.com\nwww.google.com\nwww.unknown.com\nexit\n' | java DNSClient) > $T/out 2>&1; stop
check "server answer" "from SERVER"; check "cache hit" "from CACHE"; check "NXDOMAIN" "NXDOMAIN"

echo "== Q6"; (cd Q06_ARP_Cache_Spoofing && printf '1\n192.168.1.20\n1\n192.168.1.20\n3\n192.168.1.1\n5\n3\n192.168.1.20\n6\n' | java ARPSimulator) > $T/out
check "cache hit" "CACHE HIT"; check "spoof blocked" "BLOCKED"; check "poisoned when unprotected" "CACHE POISONED"

echo "== Q7"; server Q07_RARP_Server RARPServer
(cd Q07_RARP_Server && printf 'AA:BB:CC:11:22:33\n11:22:33:44:55:01\nexit\n' | java RARPClient) > $T/out 2>&1; stop
check "static RARP" "192.168.1.11"; check "dynamic lease" "192.168.1.100"

echo "== Q8"; (cd Q08_Sliding_Window_CRC && printf '8\n4\n30\n1011\n' | java SlidingWindowCRC) > $T/out
check "errors detected" "ERROR detected"; check "all delivered" "All frames transmitted successfully"

echo "== Q9"; (cd Q09_Distance_Vector_PoisonReverse && printf '5\n0 2 999 1 999\n2 0 3 999 999\n999 3 0 4 1\n1 999 4 0 999\n999 999 1 999 0\n3 5\n' | java DistanceVectorSim) > $T/out
check "count-to-infinity shown" "YES - costs keep climbing"; check "poison reverse fixes it" "Poison Reverse     5"

echo "== Q10"; (cd Q10_Link_State_Dijkstra && printf '5\n0 10 0 30 100\n10 0 50 0 0\n0 50 0 20 10\n30 0 20 0 60\n100 0 10 60 0\n0\n2 4\n' | java LinkStateSim) > $T/out
check "manual result 0->4 = 60" "0 -> 3 -> 2 -> 4"; check "after failure 0->4 = 90" "to 4: 60 -> 90"

echo "== Q11"; (cd Q11_Routing_Protocol_Comparison && java RoutingSimulator) > $T/out
check "conclusion" "OSPF (link state)"

echo "== Q12"; ls Q12_PacketTracer_VLAN_Campus/configs/*.txt Q12_PacketTracer_VLAN_Campus/reference_pkt/*.pkt > $T/out 2>&1
check "router config present" "R1_router.txt"; check "reference pkt (PT 8.2.2) present" "Campus_VLAN_PT8.2.2_Albion.pkt"

find Q* -name "*.class" -delete; rm -rf "$T"
echo; echo "TOTAL: $PASS passed, $FAIL failed"; [ $FAIL -eq 0 ]
