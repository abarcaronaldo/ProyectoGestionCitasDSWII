# Etapa 1: compilar el Angular con Node
FROM node:20-alpine AS build
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm ci
COPY . .
RUN npm run build

# Etapa 2: servir los archivos estáticos con Nginx
FROM nginx:alpine
# Config de Nginx para que las rutas de Angular (SPA) no den 404
COPY nginx.conf /etc/nginx/conf.d/default.conf
# El builder de Angular 18 deja el resultado en dist/frontend/browser
COPY --from=build /app/dist/frontend/browser /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
