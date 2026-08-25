#!/bin/sh

echo "===== Generating pgAdmin servers.json ====="

envsubst < /pgadmin4/servers.json.template > /pgadmin4/servers.json

echo "===== Generated servers.json ====="
cat /pgadmin4/servers.json
echo "====================================="

exec /entrypoint.sh "$@"