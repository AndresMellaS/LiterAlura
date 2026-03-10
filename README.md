# LiterAlura 📚

Catálogo de libros del dominio público construido con **Spring Boot**, consumiendo la API de [Gutendex](https://gutendex.com/). Permite buscar, registrar y consultar libros y autores desde una interfaz de consola, una API REST y un frontend web.

---

## Tecnologías

- Java 25 · Spring Boot 4.0.3
- Spring Data JPA · Hibernate 7.2.4
- PostgreSQL 18
- Jackson · Maven
- HTML / CSS / JavaScript (frontend standalone)

---

## Funcionalidades

| # | Función | Consola | API REST | Frontend |
|---|---------|:-------:|:--------:|:--------:|
| 1 | Buscar libro por título | ✅ | ✅ | ✅ |
| 2 | Listar todos los libros | ✅ | ✅ | ✅ |
| 3 | Listar todos los autores | ✅ | ✅ | ✅ |
| 4 | Autores vivos en un año | ✅ | ✅ | ✅ |
| 5 | Libros por idioma | ✅ | ✅ | ✅ |
| 6 | Top 10 más descargados | ✅ | ✅ | ✅ |
| 7 | Carga masiva desde API | ✅ | ✅ | ✅ |

---

## Capturas

### Consola — Menú principal
![Menú consola](assets/consola-menu.png)

### Consola — Búsqueda de libro
![Búsqueda consola](assets/consola-buscar.png)

### Consola — Top 10 más descargados
![Top 10 consola](assets/consola-top10.png)

### Consola — Autores vivos en un año
![Autores vivos consola](assets/consola-autores-vivos.png)

### Frontend — Inicio con estadísticas
![Frontend inicio](assets/frontend-inicio.png)

### Frontend — Búsqueda de libro
![Frontend búsqueda](assets/frontend-buscar.png)

### Frontend — Top 10
![Frontend top 10](assets/frontend-top10.png)

### API REST — GET /api/libros/top10 (Postman)
![Postman top10](assets/postman-top10.png)

### API REST — GET /api/autores/vivos/{año} (Postman)
![Postman autores vivos](assets/postman-autores-vivos.png)

### API REST — POST /api/libros/buscar (Postman)
![Postman buscar](assets/postman-buscar.png)

---

## Endpoints REST

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/libros` | Todos los libros |
| GET | `/api/libros/idioma/{idioma}` | Libros por idioma (es, en, fr, pt, zh) |
| GET | `/api/libros/top10` | Top 10 más descargados |
| POST | `/api/libros/buscar` | Buscar y guardar libro por título |
| POST | `/api/libros/cargar` | Cargar libros desde Gutendex |
| GET | `/api/autores` | Todos los autores |
| GET | `/api/autores/vivos/{año}` | Autores vivos en un año dado |

### Ejemplo — Buscar libro
```http
POST /api/libros/buscar
Content-Type: application/json

{
    "titulo": "don quijote"
}
```

---

## Configuración

### Requisitos
- Java 21+
- PostgreSQL
- Maven

### Variables de entorno

Crea un archivo `.env` basado en `.env.example`:

```env
DB_URL=jdbc:postgresql://localhost:5432/literalura
DB_USERNAME=tu_usuario
DB_PASSWORD=tu_password
SPRING_PROFILE=dev
```

Configura estas variables en **IntelliJ → Run → Edit Configurations → Environment variables**.

### Perfiles
- `dev` — muestra SQL en consola, logs detallados
- `prod` — silencioso, solo errores

### Ejecutar
```bash
mvn spring:boot run
```

La app inicia el menú de consola y el servidor en `http://localhost:8080` simultáneamente.

---

## Estructura del proyecto

```
src/main/java/com/example/literalura/
├── config/
│   └── CorsConfig.java
├── controller/
│   ├── AutorController.java
│   └── LibroController.java
├── dto/
│   ├── DatosAutor.java
│   ├── DatosLibro.java
│   └── DatosResultados.java
├── model/
│   ├── Autor.java
│   └── Libro.java
├── principal/
│   └── Principal.java
├── repository/
│   ├── AutorRepository.java
│   └── LibroRepository.java
├── service/
│   ├── AutorService.java
│   ├── GutendexClient.java
│   └── LibroService.java
└── LiterAluraApplication.java
```

---

## Frontend

El archivo `index.html` es un cliente standalone que consume la API REST. Ábrelo directamente en el navegador con la app corriendo. No requiere instalación ni servidor adicional.

---

## Autor

**Andrés Mellas** · Challenge Backend — Oracle Next Education + Alura Latam
