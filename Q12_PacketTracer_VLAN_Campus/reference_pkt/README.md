# Ready-made reference .pkt file for Q12

| File | Saved in | Opens in | Use on | In GitHub repo? |
|---|---|---|---|---|
| `Campus_VLAN_PT8.2.2_Albion.pkt` | PT **8.2.2** | PT 8.2.x **and** 9.x | **College Windows PCs** (and laptop) | Yes (MIT license) |

> Packet Tracer opens files from older versions but **not** from newer ones. This PT 8.2.2 file therefore opens on both the college PCs (8.x) and the laptop (9.x).

## Campus_VLAN_PT8.2.2_Albion.pkt
- **Source:** https://github.com/ro-drick/Albion-University-Network-Design (file `campus-network.pkt`), © 2024 Rodrick Cheruiyot, **MIT License** (see `LICENSE-Albion.txt`).
- **Saved with:** Packet Tracer **8.2.2**, so it opens in PT 8.2.x (Windows college PCs) and PT 9.x (Linux).
- **Contents (checked by decoding the file):**
  - 3 × Router 2911: Main campus, Branch campus, Cloud.
  - 2 × Multilayer Switch 3560, 10 × Switch 2960, 10 PCs, 10 printers, 3 servers, 37 cables.
  - **Router-on-a-stick** on the main campus router: sub-interfaces with `encapsulation dot1Q` for VLAN 10 Admin, 20 Finance, 30 HR, 40 Business, 50 Eng&Comp, 60 Art&Design, **70 Student Lab**, 80 IT.
  - Branch campus: VLAN 90 **Staff**, 100 **Student Lab**.
  - **DHCP pool per VLAN** on the routers; PCs are set to DHCP.
  - **RIP** between the campus routers and the cloud router (10.10.10.0/30 links).
- **Known gaps:** the WEB and FTP servers have no IP configured, and there is no ACL or port security. My `../configs/` design adds those (ACL 110 blocking Labs → Admin, port security, management VLAN).

## How it maps to Q12 (labs, staff rooms, admin offices)
| Q12 requirement | In this file |
|---|---|
| Labs | VLAN 70 Student Lab (main), VLAN 100 Student Lab (branch) |
| Staff rooms | VLAN 90 Staff (branch), faculty VLANs 40–60 |
| Administrative offices | VLAN 10 Admin, 20 Finance, 30 HR |
| VLANs + routers + switches | 8 + 2 VLANs, router-on-a-stick, 3560 core + 2960 access switches |

## Use in the exam
1. Open it: `../open_packet_tracer.bat reference_pkt\Campus_VLAN_PT8.2.2_Albion.pkt` (Windows) or `../open_packet_tracer.sh reference_pkt/Campus_VLAN_PT8.2.2_Albion.pkt` (Linux).
2. Wait for the links to turn green (or click **Fast Forward Time**). Open a PC → Desktop → Command Prompt → `ipconfig`; it should show a 192.168.x.x address from its VLAN.
3. `ping` a PC in another VLAN to show inter-VLAN routing.
4. Show `show ip interface brief` and `show ip dhcp binding` on the MAIN CAMPUS ROUTER, and `show vlan brief` on an access switch.

> Use it as a reference or a demo. If the examiner wants you to build the network yourself, follow the step-by-step procedure in `../README.md` using the `../configs/*.txt` files.
