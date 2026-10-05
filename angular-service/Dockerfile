# ---- Build ----
FROM node:24-alpine AS build
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm ci
COPY . .
RUN npm run build

# ---- Run (Angular SSR server) ----
FROM node:24-alpine
WORKDIR /app
ENV NODE_ENV=production
# 4200 keeps the browser origin in line with the backend's CORS allowed origins
ENV PORT=4200
COPY --from=build /app/dist/angular-service ./dist/angular-service
USER node
EXPOSE 4200
CMD ["node", "dist/angular-service/server/server.mjs"]
