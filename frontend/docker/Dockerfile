FROM node:26.3.0-alpine3.23 AS BUILDER
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
ARG VITE_FRONTEND_BACKEND_CONNECTION_STRING
ENV VITE_FRONTEND_BACKEND_CONNECTION_STRING=${VITE_FRONTEND_BACKEND_CONNECTION_STRING}
RUN touch ./.env \
    && echo "VITE_FRONTEND_BACKEND_CONNECTION_STRING=$VITE_FRONTEND_BACKEND_CONNECTION_STRING" >> ./.env \
    && npm run build


FROM nginx:1.30.2-alpine3.23 as DEPLOY
COPY --from=BUILDER /app/dist /var/app/imagehosting
COPY ./docker/frontend.conf.template /etc/nginx/templates/frontend.conf.template
RUN envsubst < /etc/nginx/templates/frontend.conf.template > /etc/nginx/conf.d/frontend.conf
EXPOSE 80