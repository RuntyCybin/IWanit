# ---- Build stage ----
FROM node:22-alpine AS build
WORKDIR /app

# Install dependencies from the lockfile in their own cacheable layer
COPY package.json package-lock.json ./
RUN npm ci

# Build the static bundle
COPY . .
RUN npm run build

# ---- Runtime stage ----
FROM nginx:1.27-alpine

COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80

# 127.0.0.1 rather than localhost: wget resolves localhost to ::1 first, and
# nginx only listens on IPv4 here, which made the check fail against a healthy
# server.
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s \
  CMD wget -qO- http://127.0.0.1/ >/dev/null 2>&1 || exit 1
