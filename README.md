# API de Productos - Ecommerce

Esta API proporciona endpoints RESTful para realizar operaciones CRUD sobre productos.

## Tecnologías utilizadas
- Spring Boot 3.x
- Spring Data JPA
- Thymeleaf (para las vistas)
- Base de datos: MySQL
- Gestor DB: Mysql workbench community
- Maven
- Spring Security
- Java 17

## Instalación y ejecución
1. Clonar el repositorio
2. Configurar la base de datos en application.properties
3. Configurar propiedad server.port en application.properties
4. La aplicación estará en: http://localhost:8080 puerto por defecto

## Endpoints de la API

### Productos
- GET /apiproductos/api - Obtener todos los productos
- GET /apiproductos/api/{id} - Obtener producto por ID
- POST /apiproductos/api - Crear nuevo producto
- PUT /apiproductos/api/{id} - Actualizar producto
- DELETE /apiproductos/api/{id} - Eliminar producto

### Ejemplo de JSON para creación
```json
{
    "nombre": "Producto ejemplo",
    "descripcion": "Descripción del producto",
    "imagen": "default.jpg",
    "precio": 29.99,
    "cantidad": 10
}
```

### Ejemplo de JSON para actualización
```json
{
    "nombre": "HP Color LaserJet Pro MFP M479fdw",
    "descripcion": "Impresora láser a color con Wi-Fi y duplex.",
    "imagen": "default.jpg",
    "precio": "162950",
    "cantidad": 42
}
```