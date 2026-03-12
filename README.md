# Franchise API

## Documentación de la API (Swagger en línea)

La API se encuentra actualmente desplegada y funcionando. Puedes consultar su documentación interactiva a través de Swagger UI en la siguiente URL:

👉 **[https://franchise-api.cloud/swagger-ui/index.html](https://franchise-api.cloud/swagger-ui/index.html)**

---

## Pruebas (Tests)

### Ambos archivos de test
```bash
./mvnw test -Dtest="FranchiseUseCaseImplTest,FranchiseControllerTest"
```

### Solo lógica de negocio
```bash
./mvnw test -Dtest="FranchiseUseCaseImplTest"
```

### Solo controlador
```bash
./mvnw test -Dtest="FranchiseControllerTest"
```

### Todos los tests del proyecto
```bash
./mvnw test
```

## Despliegue local con Docker

Para levantar la aplicación y su base de datos (MongoDB) en un entorno local, asegúrate de tener [Docker](https://www.docker.com/) instalado.

Ejecuta el siguiente comando en la raíz del proyecto:

```bash
docker compose up -d --build
```

Esto descargará la imagen de MongoDB, construirá la imagen de la API y levantará ambos contenedores en segundo plano. La API estará disponible en el puerto `8088`.

Para detener los contenedores, ejecuta:

```bash
docker compose down
```
