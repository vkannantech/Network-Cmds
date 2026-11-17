# Exercise 10: Cisco Packet Tracer Network Design Checklist

## 1. Physical Topology
- [ ] Wireless Router (`WRT300N`): SSID `HomeNetwork`, DNS `208.67.220.220`.
- [ ] PC: Wired to Ethernet 1, DHCP mode (`192.168.0.x`).
- [ ] Laptop: Insert `WPC300N` wireless module, connect to `HomeNetwork`.
- [ ] Cable Modem: Port 1 to Router Internet; Port 0 to Cloud Coaxial.
- [ ] Cloud: Map Coaxial to Ethernet, Provider: Cable.
- [ ] Cisco.com Server: Static IP `208.67.220.220`, Gateway `208.67.220.1`, DNS `208.67.220.220`.

## 2. Server Services
- [ ] DHCP: Pool `DHCPpool`, Start IP `208.67.220.1`, Gateway `208.67.220.220`, DNS `208.67.220.220`.
- [ ] DNS: Name `Cisco.com`, Type `A Record`, Address `208.67.220.220`.

## 3. Verification
- [ ] On PC: `ipconfig /renew` (verifies `192.168.0.x`).
- [ ] On PC: `ping Cisco.com` (4 replies received from `208.67.220.220`).
- [ ] Save as `Network_Design_Networks-Commands-Deeply.pkt`.
