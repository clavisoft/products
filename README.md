# ms-products - Gestión de Productos

> Este microservicio es parte del proyecto original: [Repositorio principal](https://github.com/KezelHugo/parcial)
> 
> En el repositorio original están la colección de Postman y el informe de Sonar.


`ms-products` se encarga de la gestión de productos de un sistema e-commerce. Permite realizar operaciones CRUD, búsqueda por nombre y validación de stock.

---

## Cómo ejecutar el microservicio

### Requisitos

- Java 17+
- Maven
- MySQL

### Base de datos

Ambos microservicios usan la misma base de datos (`parcial_db`). Solo necesitas crearla una vez:

```sql
CREATE DATABASE parcial_db;
```

### Ejecutar

```bash
cd msproducts
mvn spring-boot:run
```

O ejecuta `Application.java` desde tu IDE.

---

## Endpoints principales

| Método | Endpoint                                      | Descripción                                      |
|--------|-----------------------------------------------|--------------------------------------------------|
| POST   | `/products`                                   | Crear un nuevo producto                          |
| GET    | `/products/search?name=monitor&page=0&size=5` | Buscar productos por nombre parcial y paginación |

---

## Funcionalidades

- CRUD completo de productos.
- Validación de stock (`/validate`).
- Disminución de stock (`/decrease-stock`).
- Paginación y búsqueda parcial.

---

## Patrones aplicados

- **Builder Pattern** en entidad `Product`.
- **DTO Mapping** con `ModelMapper`.
- **Validaciones** con `@Valid`, `@NotBlank`, `@Min`.
- **Manejo de errores global** con `@ControllerAdvice`.

---

## Autor

Kepler perez
