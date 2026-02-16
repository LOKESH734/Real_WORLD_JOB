# -------- Build stage --------
FROM node:18.19.1 AS build
WORKDIR /app

# Copy dependency files first (cache friendly)
COPY package.json package-lock.json ./

RUN npm ci

# Copy source code
COPY . .

# Build CRA app (outputs to /build)
RUN npm run build && \
    test -d build


# -------- Runtime stage --------
FROM nginx:1.25

# Clean default nginx content
RUN rm -rf /usr/share/nginx/html/*

# Copy CRA build output
COPY --from=build /app/build /usr/share/nginx/html

# SPA routing support
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
