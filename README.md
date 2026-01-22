# Pet Insurance - Microservices Application

Sistema de seguros para mascotas basado en microservicios con Spring Boot.

## 📋 Arquitectura

La solución está compuesta por:

- **Quoting Service** (Puerto 8080): Servicio de cotizaciones
- **Policy Service** (Puerto 8081): Servicio de pólizas
- **MongoDB Quoting** (Puerto 27017): Base de datos para cotizaciones
- **MongoDB Policy** (Puerto 27018): Base de datos para pólizas

## 🔧 Prerequisitos

Antes de ejecutar la aplicación, asegúrate de tener instalado:
- [Docker](https://www.docker.com/get-started) (versión 20.10 o superior)


## 🚀 Cómo Ejecutar el proyecto

### Opción 1: Iniciar todos los servicios

```bash
docker-compose up -d
```

Este comando:
- Descarga las imágenes de MongoDB si no están disponibles
- Construye las imágenes de los microservicios desde el código fuente
- Inicia todos los contenedores en modo detached (segundo plano)

### Opción 2: Ver los logs en tiempo real

```bash
docker-compose up
```


## ✅ Verificar que los Servicios Están Funcionando

### Verificar el estado de los contenedores

```bash
docker ps
```

Deberías ver 4 contenedores en ejecución:
- `quoting-service`
- `policy-service`
- `mongo-quoting`
- `mongo-policy`



## 📝 Endpoints Disponibles

### Quoting Service (Puerto 8080)

#### 1. **POST /quotations** - Crear cotización
Genera una nueva cotización para seguro de mascotas.

**Request:**
```json
{
  "name": "Max",
  "species": "dog",
  "breed": "Golden Retriever",
  "age": 3,
  "premium": true
}
```

**Response:**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "petName": "Max",
  "species": "dog",
  "breed": "Golden Retriever",
  "age": 3,
  "premiumPlan": true,
  "price": 49.99,
  "expiresAt": "2026-02-22",
  "expired": false
}
```

#### 2. **GET /quotations** - Listar todas las cotizaciones
Obtiene todas las cotizaciones generadas.

**Response:**
```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "petName": "Max",
    "species": "dog",
    "breed": "Golden Retriever",
    "age": 3,
    "premiumPlan": true,
    "price": 49.99,
    "expiresAt": "2026-02-22",
    "expired": false
  },
  {
    "id": "660e9511-f39c-52e5-b827-557766551111",
    "petName": "Luna",
    "species": "cat",
    "breed": "Persian",
    "age": 2,
    "premiumPlan": false,
    "price": 29.99,
    "expiresAt": "2026-02-22",
    "expired": false
  }
]
```

#### 3. **GET /quotations/{id}** - Obtener cotización por ID
Obtiene una cotización específica por su ID.

**Response:**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "petName": "Max",
  "species": "dog",
  "breed": "Golden Retriever",
  "age": 3,
  "premiumPlan": true,
  "price": 49.99,
  "expiresAt": "2026-02-22",
  "expired": false
}
```


### Policy Service (Puerto 8081)

#### 4. **POST /policies** - Emitir póliza
Emite una póliza de seguro basada en una cotización existente.

**Request:**
```json
{
  "quotationId": "550e8400-e29b-41d4-a716-446655440000",
  "ownerId": "USR-12345",
  "ownerName": "Juan Pérez",
  "ownerEmail": "juan.perez@email.com"
}
```

**Response:**
```json
{
  "policyId": "770e9511-f39c-52e5-b827-557766551234",
  "quotationId": "550e8400-e29b-41d4-a716-446655440000",
  "active": true
}
```

### 💡 Flujo de Uso Completo

1. **Crear una cotización** para una mascota
2. **Consultar las cotizaciones** disponibles
3. **Emitir una póliza** usando el ID de la cotización
