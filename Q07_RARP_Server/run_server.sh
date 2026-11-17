#!/usr/bin/env bash
# Linux: compiles then runs RARPServer. Extra arguments are passed to the program.
cd "$(dirname "$0")" || exit 1
javac *.java || exit 1
java RARPServer "$@"
