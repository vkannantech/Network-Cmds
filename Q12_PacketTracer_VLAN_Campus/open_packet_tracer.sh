#!/usr/bin/env bash
# Linux: open Cisco Packet Tracer (pass a .pkt file to open it)
cd "$(dirname "$0")" || exit 1
if command -v packettracer >/dev/null; then packettracer "$@" >/dev/null 2>&1 &
elif [ -x /opt/pt/packettracer ]; then /opt/pt/packettracer "$@" >/dev/null 2>&1 &
else echo "Packet Tracer not found - install from netacad.com"; fi
