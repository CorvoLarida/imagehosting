FROM nginx:1.30.2-alpine3.23
COPY ./docker/*.conf.template /etc/nginx/templates/
RUN envsubst < /etc/nginx/templates/imagehosting.conf.template > /etc/nginx/conf.d/imagehosting.conf \
 && envsubst < /etc/nginx/templates/imagehosting.old.conf.template > /etc/nginx/conf.d/imagehosting.old.conf