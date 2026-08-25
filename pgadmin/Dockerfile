FROM dpage/pgadmin4:latest

USER root

RUN apk add --no-cache gettext

COPY servers.json.template /pgadmin4/servers.json.template
COPY entrypoint.sh /pgadmin4/custom-entrypoint.sh

RUN chmod +x /pgadmin4/custom-entrypoint.sh

RUN PGADMIN_UID=$(id -u pgadmin) && \
    chown ${PGADMIN_UID}:0 /pgadmin4/servers.json.template && \
    chmod 664 /pgadmin4/servers.json.template && \
    touch /pgadmin4/servers.json && \
    chown ${PGADMIN_UID}:0 /pgadmin4/servers.json && \
    chmod 664 /pgadmin4/servers.json

ENTRYPOINT ["/pgadmin4/custom-entrypoint.sh"]

USER pgadmin