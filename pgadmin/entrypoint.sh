#!/bin/sh

envsubst < /pgadmin4/servers.json.template > /pgadmin4/servers.json

exec /entrypoint.sh "$@"