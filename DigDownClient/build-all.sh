#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")" && pwd)"
(cd "$ROOT/67client/67client" && ./gradlew build)
(cd "$ROOT/Anubis/anubis-1.0-src" && ./gradlew build)
echo "DigDown Client build complete. Check each project's build/libs folder."
