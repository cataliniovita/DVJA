FROM node:18 AS build
WORKDIR /app
COPY package.json ./
RUN npm install
COPY . .
RUN npm run build

# Step 2: Serve with Node.js
FROM node:18-alpine
WORKDIR /app
COPY --from=build /app/build ./build
COPY package.json ./
RUN npm install --omit=dev  # Only install production dependencies

# Install and use serve (or another lightweight HTTP server)
RUN npm install -g serve

EXPOSE 3000
CMD ["serve", "-s", "build", "-l", "3000"]
