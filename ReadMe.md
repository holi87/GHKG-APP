# 🚗 GHKG-API — running on Docker

## ✅ Requirements

- [Docker Desktop](https://www.docker.com/products/docker-desktop) installed on your machine

---

## 🐳 Step 1: download image

```bash
docker pull gholak87/ghkg-api:latest
```

## 🐳 Step 2: run container

```bash
docker run \
  -e SERVER_PORT=8080 \
  -e SERVER_ADDRESS=0.0.0.0 \
  -p 8081:8080 \
  gholak87/ghkg-api:latest
```

The application uses these default values (defined in application.yaml):

```yaml
server:
  port: ${SERVER_PORT:8080}
  address: ${SERVER_ADDRESS:localhost}
```

## 🐳 Step 3: run swagger

```bash 
http://localhost:8081/swagger-ui/index.html
```

## 🐳 Step 4: basic admin credentials

```bash
usernames: super, admin, user, worker
password: Tesla.123
```

## 🐳 Step 5: happy testing!