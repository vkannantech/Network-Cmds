#!/usr/bin/env bash
# Compile every Java program in Q01-Q12 (Linux)
cd "$(dirname "$0")" || exit 1
ok=0; fail=0
for d in Q*/; do
  ls "$d"*.java >/dev/null 2>&1 || continue
  if (cd "$d" && javac *.java); then echo "[OK]   $d"; ok=$((ok+1)); else echo "[FAIL] $d"; fail=$((fail+1)); fi
done
echo "Compiled: $ok OK, $fail failed"
