# Етап 1: збірка
FROM node:18 AS build-stage

WORKDIR /app
COPY package*.json ./
RUN npm install
RUN chmod +x ./node_modules/.bin/tsc
COPY . .
RUN npm run build

# Етап 2: Nginx
FROM nginx:stable-alpine

# Копіюємо кастомний nginx.conf у контейнер
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Копіюємо зібрані файли з першого етапу
COPY --from=build-stage /app/dist /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
