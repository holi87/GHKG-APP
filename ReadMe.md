# 🚗 GHKG-APP — running on Docker

## ✅ Requirements

- [Docker Desktop](https://www.docker.com/products/docker-desktop) installed on your machine

---

## 🐳 Step 1: download image from GitHub Container Registry

```bash
docker pull ghcr.io/holi87/ghkg-app:latest
```

## 🐳 Step 2: run container

```bash
docker run gholak87/ghkg-app:latest
```

The application uses these default values (defined in application.yaml):

```yaml
server:
  port: ${PORT:8080}
  address: ${SERVER_ADDRESS:localhost}
```

## 🐳 Step 3: webapp

```bash 
http://localhost:8080
```

## 📚 OpenAPI / Swagger

- Spec JSON: `http://localhost:8080/v3/api-docs`
- UI: `http://localhost:8080/swagger-ui/index.html`

Auth dla endpointów zabezpieczonych: nagłówek `Authorization: Bearer <JWT>`.

## 🐳 Step 4: basic credentials

```bash
usernames: super, admin, user, worker
password: Tesla.123
```

## 🐳 Step 5: happy testing!