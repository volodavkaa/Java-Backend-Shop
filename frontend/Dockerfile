# Етап 1: збірка
FROM node:18 AS build

WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# Етап 2: Nginx
FROM nginx:stable-alpine

# Копіюємо КАСТОМНИЙ nginx.conf у контейнер
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Копіюємо зібрані файли з першого етапу
COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
