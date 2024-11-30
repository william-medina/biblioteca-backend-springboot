# BIBLIOTECA Backend
![Framework](https://img.shields.io/badge/Framework-Spring%20Boot-green)
![Version](https://img.shields.io/badge/version-1.0.0-green)
![Database](https://img.shields.io/badge/Database-MySQL-orange)
![Language](https://img.shields.io/badge/Language-Java%2021-white)
![API Documentation](https://img.shields.io/badge/API%20Docs-Swagger-green)

## Descripción General
El backend de **BIBLIOTECA** es un sistema de gestión de API RESTful diseñado para una biblioteca personal. Proporciona funcionalidades clave para la autenticación de usuarios y la gestión de libros, incluyendo inicio de sesión seguro, gestión de colecciones y carga de imágenes de portadas. Construido con Spring Boot y MySQL, este sistema facilita la organización y el control de los libros en una biblioteca privada.

## Características

El backend ofrece las siguientes funcionalidades clave:

### 1. **Autenticación de Usuarios**
- **Inicio de Sesión:** Maneja el inicio de sesión de usuarios, verificando credenciales y creando sesiones de usuario.
- **Recuperar Datos del Usuario:** Proporciona acceso a los detalles del usuario conectado.

### 2. **Gestión de Libros**
- **Agregar Nuevos Libros:** Permite la adición de nuevos libros a la colección de la biblioteca.
- **Actualizar Información del Libro:** Habilita la actualización de detalles de libros existentes.
- **Eliminar Libros:** Soporta la eliminación de libros de la colección de la biblioteca.
- **Obtener Todos los Libros:** Recupera una lista de todos los libros en la biblioteca, opcionalmente ordenada por un campo específico (por ejemplo, título, autor, editorial).
- **Obtener Conteo Total de Libros:** Proporciona el número total de libros en la colección.
- **Obtener Libros Aleatorios:** Recupera una selección aleatoria de libros basada en un conteo especificado.
- **Obtener Libros por Ubicación:** Recupera libros organizados por su ubicación dentro de la biblioteca.
- **Buscar Libros:** Busca libros usando palabras clave en títulos, autores, editoriales, año de publicación y ISBN.
- **Obtener Libro por ISBN:** Recupera información detallada para un libro específico usando su ISBN.

### 3. **Carga de Portadas de Libros**
- **Carga de Archivos:** La aplicación soporta la carga de archivos para portadas de libros, permitiendo a los usuarios subir imágenes con las siguientes restricciones:
    - **Tipo de Archivo:** Solo imágenes `.jpg` son permitidas.
    - **Límite de Tamaño de Archivo:** Los archivos deben ser de 5MB o menores.
- **Cómo Funciona:**
    - **Configuración:** El sistema de archivos está configurado para usar almacenamiento público para el manejo permanente de archivos.
    - **Validación de Archivos:** Solo archivos `.jpg` con el tipo MIME `image/jpeg` son aceptados.
    - **Manejo de Errores:** Si un archivo cargado excede el límite de tamaño o tiene un tipo inválido, la aplicación responde con un mensaje de error específico.
    - **Nombre del Campo:** El nombre del campo para la imagen de portada cargada en el formulario debe ser `cover`.

## Tecnologías

El backend de **BIBLIOTECA** está construido utilizando las siguientes tecnologías modernas:

- **Spring Boot**: Un potente framework basado en Java para la construcción de aplicaciones web, que proporciona una sintaxis elegante y características robustas para el desarrollo rápido.
- **MySQL**: Una base de datos relacional que se utiliza para almacenar y gestionar los datos de libros y usuarios en el sistema.
- **Hibernate**: Una herramienta de Mapeo Objeto-Relacional (ORM) utilizada para la interacción con la base de datos, facilitando el acceso a los datos mediante objetos Java en lugar de consultas SQL.
- **Spring Security**: Un módulo de seguridad en Spring que proporciona una manera de autenticar y autorizar usuarios, asegurando las aplicaciones web con control de acceso.
- **Spring Data JPA**: Proporciona integración con JPA (Java Persistence API) y facilita el uso de bases de datos en Spring mediante repositorios y entidades.
- **JWT (JSON Web Tokens)**: Utilizado para la autenticación y autorización de usuarios, generando tokens seguros para mantener las sesiones activas.
- **Swagger (Springdoc OpenAPI)**: Herramienta que genera documentación interactiva de la API de manera automática, permitiendo visualizar los endpoints, sus parámetros y respuestas.
- **Lombok**: Una biblioteca Java que reduce el boilerplate code generando automáticamente métodos como `getters`, `setters`, y `constructores` mediante anotaciones.

## Requisitos Previos

Antes de comenzar, asegúrate de haber cumplido con los siguientes requisitos:

- [Java](https://www.oracle.com/java/technologies/javase-downloads.html) (Recomendado: Java 21 o una versión compatible)
- [Maven](https://maven.apache.org/install.html) (Gestor de proyectos para Java)
- [MySQL](https://dev.mysql.com/downloads/installer/) (Instalado y funcionando localmente o usando una instancia en la nube)

## Instalación

1. **Clona el repositorio:**

    ```bash
    git clone https://github.com/william-medina/biblioteca-backend-springboot.git
    ```

2. Navega al directorio del proyecto:

    ```bash
    cd biblioteca-backend-springboot
    ```

3. **Instala las dependencias:**

   Asegúrate de que las dependencias del proyecto estén instaladas. Esto se puede hacer automáticamente utilizando el archivo `pom.xml` de Maven, el cual gestionará las dependencias necesarias para la aplicación.


4. **Configura las variables de entorno:**

   Agrega las siguientes variables de entorno necesarias para el funcionamiento de la aplicación. Esto puede hacerse ya sea en tu IDE o en tu sistema operativo:

    ```dotenv
    # Base de Datos
    DB_URL=jdbc:mysql://localhost:3306/your_database_name
    DB_USERNAME=your_username
    DB_PASSWORD=your_password

    # Clave Secreta para JWT
    JWT_SECRET=your_secret_jwt

    # URL del Frontend - Habilita CORS para permitir peticiones desde esta URL
    FRONTEND_URL=http://localhost:5173
    ```

   Reemplaza los valores de ejemplo con los detalles de tu configuración real.


5. **Inicia la aplicación:**

   La clase principal de la aplicación es `BibliotecaApplication`. Ejecuta esta clase para iniciar el servidor.


 6. **Configuración para la carga de archivos:**

    En la raíz del proyecto, crea una carpeta llamada `uploads` y dentro de ella, una subcarpeta denominada `covers`. Esta estructura se utilizará para almacenar las portadas de los libros. Las imágenes se guardarán en la carpeta `uploads/covers`, y podrás acceder a ellas mediante rutas relativas en la API, por ejemplo:

    - `http://localhost:8080/api/covers/cover.jpg`


## Arquitectura

El backend de **BIBLIOTECA** sigue la arquitectura **Modelo-Vista-Controlador (MVC)**:

### 1. **Modelo**

- **Ubicación:** `src/main/java/com/williammedina/biblioteca/models`
- **Responsabilidades:** Define la estructura de datos para la aplicación (por ejemplo, Libros, Usuarios), maneja interacciones con la base de datos usando **JPA** (Java Persistence API) y **Hibernate**, e implementa la lógica de negocio relacionada con los datos.

### 2. **Vista**

- **Ubicación:** No aplicable directamente; las APIs de Spring Boot típicamente devuelven respuestas JSON, que sirven como "vista."
- **Responsabilidades:** Proporciona respuestas JSON formateadas para solicitudes API, que son consumidas por el frontend u otros servicios.

### 3. **Controlador**

- **Ubicación:** `src/main/java/com/williammedina/biblioteca/controllers`
- **Responsabilidades:** Procesa las solicitudes entrantes, se comunica con los modelos (repositorios) para manejar los datos y devuelve respuestas al cliente (generalmente en formato JSON) utilizando anotaciones como `@RestController` y `@RequestMapping`.

### Documentación de la API
- La documentación de la API está disponible a través de [Swagger UI](http://localhost:8080/api/docs/swagger-ui/index.html) una vez que la aplicación esté en funcionamiento. Esta herramienta te permite explorar todos los endpoints disponibles y realizar pruebas directamente desde tu navegador, facilitando la interacción con la API.
   ```
   http://localhost:8080/api/docs/swagger-ui/index.html
   ```

## API Endpoints

### Rutas de Libros

| **Endpoint**                   | **Método** | **Descripción**                                                  |
|--------------------------------|------------|------------------------------------------------------------------|
| `/api/books/count`             | `GET`      | Obtiene el conteo total de libros en la biblioteca.              |
| `/api/books/random/{count}`    | `GET`      | Obtiene una selección aleatoria de libros según el conteo especificado. |
| `/api/books/location`          | `GET`      | Obtiene libros organizados por su ubicación en la biblioteca.    |
| `/api/books/{sortBy}`          | `GET`      | Obtiene todos los libros ordenados por un campo específico (por ejemplo, título, autor, editorial). |
| `/api/books/search/{keyword}`  | `GET`      | Busca libros por una palabra clave en títulos, autores, editoriales, año de publicación e ISBN. |
| `/api/books/isbn/{isbn}`       | `GET`      | Obtiene información detallada de un libro específico por su ISBN. |
| `/api/books`                   | `POST`     | Añade un nuevo libro a la biblioteca.                            |
| `/api/books/{isbn}`            | `PUT`      | Actualiza la información de un libro específico por su ISBN.     |
| `/api/books/{isbn}`            | `DELETE`   | Elimina un libro específico de la biblioteca.                    |

### Rutas de Autenticación

| **Endpoint**                   | **Método** | **Descripción**                                                  |
|--------------------------------|------------|------------------------------------------------------------------|
| `/api/auth/login`              | `POST`     | Autentica a un usuario y devuelve un token JWT.                  |
| `/api/auth/me`                 | `GET`      | Obtiene los detalles del usuario autenticado actualmente.        |

### Rutas de Portadas

| **Endpoint**                   | **Método** | **Descripción**                                                  |
|--------------------------------|------------|------------------------------------------------------------------|
| `/api/covers/{filename}`       | `GET`      | Permite obtener la portada de un libro específica, utilizando el nombre del archivo. |

## Autor

Esta aplicación backend para **BIBLIOTECA** ha sido desarrollada y es mantenida por:

**William Medina**

¡Gracias por revisar **BIBLIOTECA**!