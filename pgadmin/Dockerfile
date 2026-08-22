FROM dpage/pgadmin4:latest

USER root

RUN apk add --no-cache gettext

COPY servers.json.template /pgadmin4/servers.json.template
COPY entrypoint.sh /pgadmin4/custom-entrypoint.sh

RUN chmod +x /pgadmin4/custom-entrypoint.sh

USER pgadmin