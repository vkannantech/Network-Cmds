# Q12 – Educational Institution Network with VLANs (Cisco Packet Tracer)

## Question
> An educational institution requires a network connecting **labs, staff rooms, and administrative offices** with **VLANs, routers, and switches**. Using **Cisco Packet Tracer**, design the network topology, configure IP addressing, and simulate basic connectivity. **Justify design choices** based on **efficiency, scalability, and security**.

## Aim
To design and simulate a campus network in Cisco Packet Tracer with separate VLANs for labs, staff, admin and servers, inter-VLAN routing (router-on-a-stick), DHCP, an ACL and port security, and to verify connectivity.

## Apparatus Required
Cisco Packet Tracer 8.x (Windows) / 9.x (Linux). Devices: 1 × **Router 2911**, 3 × **Switch 2960-24TT**, 6 × PC, 1 × Server, copper straight-through cables.
Config files (copy and paste into each device's CLI): `configs/R1_router.txt`, `configs/SW-CORE.txt`, `configs/SW-LAB.txt`, `configs/SW-OFFICE.txt`.

## Topology
```
                        [R1 - 2911]
                          G0/0  (802.1Q trunk, router-on-a-stick)
                           |
                        G0/1
                     [SW-CORE 2960] Fa0/10 ---- [Server 192.168.40.10]  VLAN 40
                     Fa0/1      Fa0/2
                 (trunk)          (trunk)
                   G0/1            G0/1
             [SW-LAB 2960]      [SW-OFFICE 2960]
             Fa0/1   Fa0/2      Fa0/1   Fa0/2    Fa0/11   Fa0/12
             LAB-PC1 LAB-PC2    STAFF1  STAFF2   ADMIN1   ADMIN2
             VLAN 10            VLAN 20          VLAN 30
```

## IP Addressing Plan
| VLAN | Name | Network | Gateway (R1 sub-interface) | Hosts |
|---|---|---|---|---|
| 10 | LABS | 192.168.10.0/24 | 192.168.10.1 (G0/0.10) | Lab PCs – DHCP (.11 onward) |
| 20 | STAFF | 192.168.20.0/24 | 192.168.20.1 (G0/0.20) | Staff room PCs – DHCP |
| 30 | ADMIN | 192.168.30.0/24 | 192.168.30.1 (G0/0.30) | Admin office PCs – DHCP |
| 40 | SERVERS | 192.168.40.0/24 | 192.168.40.1 (G0/0.40) | Server **192.168.40.10** (static) |
| 99 | MANAGEMENT | 192.168.99.0/24 | 192.168.99.1 (G0/0.99) | SW-CORE .2, SW-LAB .3, SW-OFFICE .4 |

## Cabling (all copper straight-through)
| From | Port | To | Port |
|---|---|---|---|
| R1 | GigabitEthernet0/0 | SW-CORE | GigabitEthernet0/1 |
| SW-CORE | FastEthernet0/1 | SW-LAB | GigabitEthernet0/1 |
| SW-CORE | FastEthernet0/2 | SW-OFFICE | GigabitEthernet0/1 |
| SW-CORE | FastEthernet0/10 | Server | FastEthernet0 |
| SW-LAB | Fa0/1, Fa0/2 | LAB-PC1, LAB-PC2 | FastEthernet0 |
| SW-OFFICE | Fa0/1, Fa0/2 | STAFF-PC1, STAFF-PC2 | FastEthernet0 |
| SW-OFFICE | Fa0/11, Fa0/12 | ADMIN-PC1, ADMIN-PC2 | FastEthernet0 |

## Procedure (step by step, about 20 minutes)
1. **Open Packet Tracer** (`open_packet_tracer.bat` / `./open_packet_tracer.sh`).
2. **Place devices:**
   - Network Devices → Routers → **2911** (rename **R1**).
   - Switches → **2960** ×3 (**SW-CORE, SW-LAB, SW-OFFICE**).
   - End Devices → 6 PCs + 1 **Server**.
   - Rename them (click device → Config → Display Name).
3. **Cable them** with *Copper Straight-Through* exactly as in the cabling table.
4. **Configure each switch:** click it → **CLI** tab → press Enter. Open the matching file in `configs/`, copy **all** of it and paste it (right-click → Paste). Do SW-CORE, SW-LAB, SW-OFFICE.
5. **Configure the router:** R1 → CLI → answer `no` to the "initial configuration dialog" → paste `configs/R1_router.txt`.
6. **Server:**
   - Desktop → IP Configuration → Static: IP `192.168.40.10`, mask `255.255.255.0`, gateway `192.168.40.1`, DNS `192.168.40.10`.
   - Services → **DNS**: On; Name `www.college.edu`, Type A Record, Address `192.168.40.10` → Add.
   - Services → **HTTP**: On.
7. **PCs:** each PC → Desktop → IP Configuration → select **DHCP**. Each gets an address from its VLAN's pool (e.g. LAB-PC1 → 192.168.10.11).
8. Wait about 30 s for all links to turn green (spanning tree), or click **Fast Forward Time**.
9. **Save:** File → Save As → `College_VLAN_Network.pkt` in this folder.

## Verification (simulate basic connectivity)
| Test (from) | Command | Expected |
|---|---|---|
| LAB-PC1 | `ipconfig` | 192.168.10.11, gateway 192.168.10.1, DNS 192.168.40.10 |
| LAB-PC1 | `ping 192.168.10.1` | Replies (own gateway) |
| LAB-PC1 | `ping 192.168.40.10` | Replies (inter-VLAN routing to server) |
| LAB-PC1 | `ping 192.168.20.11` (staff PC) | Replies |
| LAB-PC1 | `ping 192.168.30.11` (admin PC) | **Destination host unreachable** – blocked by ACL 110 (security works) |
| STAFF-PC1 | `ping 192.168.30.11` | Replies (staff may reach admin) |
| ADMIN-PC1 | Web Browser → `http://www.college.edu` | Server's web page (DNS + HTTP) |
| R1 | `show ip interface brief` | G0/0 and G0/0.10–.99 up/up |
| R1 | `show ip dhcp binding` | One lease per PC |
| R1 | `show access-lists` | ACL 110 with match counters |
| SW-OFFICE | `show vlan brief` | Fa0/1–2 in VLAN 20, Fa0/11–12 in VLAN 30 |
| SW-CORE | `show interfaces trunk` | Gi0/1, Fa0/1, Fa0/2 trunking VLANs 1,10,20,30,40,99 |
| SW-LAB | `show port-security` | Fa0/1–2 secured, max 1 MAC |

Use **Simulation mode** (bottom right) and send a PDU from LAB-PC1 to the server to watch the frame get the 802.1Q tag on the trunk and be routed by R1.

## Key Configuration (excerpt)
```
! R1 – router-on-a-stick
interface GigabitEthernet0/0.10
 encapsulation dot1Q 10
 ip address 192.168.10.1 255.255.255.0
 ip access-group 110 in
ip dhcp pool LABS
 network 192.168.10.0 255.255.255.0
 default-router 192.168.10.1
 dns-server 192.168.40.10
access-list 110 deny ip 192.168.10.0 0.0.0.255 192.168.30.0 0.0.0.255
access-list 110 permit ip any any

! SW-LAB – access ports
interface range FastEthernet0/1 - 2
 switchport mode access
 switchport access vlan 10
 switchport port-security
 switchport port-security maximum 1
 switchport port-security mac-address sticky
interface GigabitEthernet0/1
 switchport mode trunk
```

## Design Justification (answer to "Justify")
**Efficiency**
- **VLANs split the broadcast domain:** ARP and DHCP broadcasts from 40 lab PCs never reach the admin office, which saves bandwidth and CPU.
- **Router-on-a-stick:** one router port routes all VLANs over a single 802.1Q trunk, which is cheap and easy to manage.
- **DHCP on the router:** no manual IP configuration on every PC and no address conflicts.
- **Hierarchical design:** access switches (labs/offices) → core switch → router; the server is central.
- `spanning-tree portfast` on PC ports brings them up immediately.

**Scalability**
- A new lab means a new access switch with a trunk to SW-CORE, and its ports go in VLAN 10. No re-addressing.
- A new department means one new VLAN, one sub-interface and one DHCP pool.
- **/24 per VLAN** = 254 hosts each; the 192.168.x.0 numbering leaves room for VLANs 50, 60, ….
- For heavier traffic, replace the router with a **Layer-3 switch** (SVIs) or add EtherChannel, without changing the addressing.

**Security**
- **Segmentation:** students (VLAN 10) are isolated from the administration network (VLAN 30) by **ACL 110**, while still reaching the servers and the Internet.
- **Port security** (max 1 sticky MAC, violation restrict) stops students plugging in unauthorised devices or hubs.
- **Unused ports shut down**, so there are no open jacks to plug into.
- **Management VLAN 99** keeps switch management separate from user traffic.
- `enable secret`, console and VTY passwords, `service password-encryption`, and a login banner.

## Viva Questions
1. **What is a VLAN?** A logical LAN created on a switch. Each VLAN is a separate broadcast domain.
2. **Access port vs trunk port?** An access port carries one VLAN (to an end device). A trunk carries many VLANs between switches or routers, using 802.1Q tags.
3. **What is router-on-a-stick?** Inter-VLAN routing on one physical router interface split into sub-interfaces, each with `encapsulation dot1Q <vlan>`.
4. **Why can't PCs in different VLANs talk without a router?** They are in different IP subnets and broadcast domains, so a Layer-3 device must route between them.
5. **What does an extended ACL match?** Source IP, destination IP, protocol and ports (numbers 100–199).
6. **What happens on a port-security violation in `restrict` mode?** Frames from the unknown MAC are dropped and a counter / log is raised, but the port stays up.

## Ready-made .pkt (downloaded and verified)
| File | PT version | Use on |
|---|---|---|
| `reference_pkt/Campus_VLAN_PT8.2.2_Albion.pkt` | 8.2.2 (opens in 8.x and 9.x) | College PC and laptop (opens in 8.x and 9.x) |

The PT 8.2.2 file is router-on-a-stick VLANs (Admin, HR, Finance, Student Labs, Staff) with per-VLAN DHCP and RIP. Details and demo steps are in `reference_pkt/README.md`.

## Reference
A routed campus network built earlier (3 routers with RIP, departmental LANs, DNS/WEB/FTP servers, no VLANs) is in `excer/exc-10-packet-tracer/College Network.pkt`.

## Result
A college network with VLANs for labs, staff, admin and servers was designed in Cisco Packet Tracer, with router-on-a-stick inter-VLAN routing, DHCP, an ACL and port security. Connectivity was verified with ping and web access, and the design choices were justified for efficiency, scalability and security.
