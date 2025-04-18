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
docker run -p 8081:8080 <twoj_user>/ghkg-api:latest
```

## 🐳 Step 3: run swagger

```bash 
http://localhost:8081/swagger-ui/index.html
```

## 🐳 Step 4: basic admin credentials

```bash
username: admin
password: Tesla.123
```

## 🐳 Step 5: happy testing!