# BIBLIOTECA Backend
![Framework](https://img.shields.io/badge/Framework-Spring%20Boot-green)
![Version](https://img.shields.io/badge/version-1.0.0-green)
![Database](https://img.shields.io/badge/Database-MySQL-orange)
![Language](https://img.shields.io/badge/Language-Java%2021-white)
![API Documentation](https://img.shields.io/badge/API%20Docs-Swagger-green)

## General Description
The **BIBLIOTECA** backend is a RESTful API management system designed for a personal library. It provides key functionalities for user authentication and book management, including secure login, collection management, and cover image uploads. Built with Spring Boot and MySQL, this system facilitates the organization and control of books in a private library.

## Features

The backend offers the following key functionalities:

### 1. **User Authentication**
- **Login:** Handles user login, verifying credentials and creating user sessions.
- **Retrieve User Data:** Provides access to the logged-in user's details.

### 2. **Book Management**
- **Add New Books:** Allows adding new books to the library collection.
- **Update Book Information:** Enables updating details of existing books.
- **Delete Books:** Supports deleting books from the library collection.
- **Get All Books:** Retrieves a list of all books in the library, optionally sorted by a specific field (e.g., title, author, publisher).
- **Get Total Book Count:** Provides the total number of books in the collection.
- **Get Random Books:** Retrieves a random selection of books based on a specified count.
- **Get Books by Location:** Retrieves books organized by their location in the library.
- **Search Books:** Searches for books using keywords in titles, authors, publishers, publication year, and ISBN.
- **Get Book by ISBN:** Retrieves detailed information for a specific book using its ISBN.

### 3. **Book Cover Upload**
- **File Upload:** The application supports file uploads for book covers, allowing users to upload images with the following restrictions:
    - **File Type:** Only `.jpg` images are allowed.
    - **File Size Limit:** Files must be 5MB or smaller.
- **How It Works:**
    - **Configuration:** The file system is configured to use public storage for permanent file handling.
    - **File Validation:** Only `.jpg` files with the MIME type `image/jpeg` are accepted.
    - **Error Handling:** If an uploaded file exceeds the size limit or has an invalid type, the application responds with a specific error message.
    - **Field Name:** The name of the field for the uploaded cover image in the form must be `cover`.

## Technologies

The **BIBLIOTECA** backend is built using the following modern technologies:

- **Spring Boot:** A powerful Java-based framework for building web applications, providing elegant syntax and robust features for rapid development.
- **MySQL:** A relational database used to store and manage book and user data in the system.
- **Hibernate:** An Object-Relational Mapping (ORM) tool used for database interaction, facilitating data access using Java objects instead of SQL queries.
- **Spring Security:** A Spring module that provides a way to authenticate and authorize users, securing web applications with access control.
- **Spring Data JPA:** Provides integration with JPA (Java Persistence API) and simplifies the use of databases in Spring through repositories and entities.
- **JWT (JSON Web Tokens):** Used for user authentication and authorization, generating secure tokens to maintain active sessions.
- **Swagger (Springdoc OpenAPI):** A tool that automatically generates interactive API documentation, allowing visualization of endpoints, their parameters, and responses.
- **Lombok:** A Java library that reduces boilerplate code by automatically generating methods like `getters`, `setters`, and `constructors` using annotations.

## Prerequisites

Before starting, make sure you have the following prerequisites:

- [Java](https://www.oracle.com/java/technologies/javase-downloads.html) (Recommended: Java 21 or compatible version)
- [Maven](https://maven.apache.org/install.html) (Project management tool for Java)
- [MySQL](https://dev.mysql.com/downloads/installer/) (Installed and running locally or using a cloud instance)

## Installation

1. **Clone the repository:**

    ```bash
    git clone https://github.com/william-medina/biblioteca-backend-springboot.git
    ```

2. Navigate to the project directory:

    ```bash
    cd biblioteca-backend-springboot
    ```

3. **Install dependencies:**

   Ensure that the project dependencies are installed. This can be done automatically using the `pom.xml` file with Maven, which will manage the necessary dependencies for the application.


4. **Configure environment variables:**

   Add the following environment variables necessary for the application's operation. You can do this either in your IDE or in your operating system:

    ```dotenv
    # Database
    DB_URL=jdbc:mysql://localhost:3306/your_database_name
    DB_USERNAME=your_username
    DB_PASSWORD=your_password

    # JWT Secret Key
    JWT_SECRET=your_secret_jwt

    # Frontend URL - Enable CORS to allow requests from this URL
    FRONTEND_URL=http://localhost:5173
    ```

   Replace the example values with your actual configuration details.


5. **Start the application:**

   The main application class is `BibliotecaApplication`. Run this class to start the server.


6. **File Upload Configuration:**

    In the root of the project, create a folder named `uploads` and inside it, a subfolder called `covers`. This structure will be used to store the book covers. The images will be saved in the `uploads/covers` folder, and you can access them through relative paths in the API, such as:

   - `http://localhost:8080/api/covers/cover.jpg`

## Architecture

The **BIBLIOTECA** backend follows the **Model-View-Controller (MVC)** architecture:

### 1. **Model**

- **Location:** `src/main/java/com/williammedina/biblioteca/models`
- **Responsibilities:** Defines the data structure for the application (e.g., Books, Users), handles interactions with the database using **JPA** (Java Persistence API) and **Hibernate**, and implements the business logic related to the data.

### 2. **View**

- **Location:** Not directly applicable; Spring Boot APIs typically return JSON responses that serve as the "view."
- **Responsibilities:** Provides formatted JSON responses for API requests, which are consumed by the frontend or other services.

### 3. **Controller**

- **Location:** `src/main/java/com/williammedina/biblioteca/controllers`
- **Responsibilities:** Processes incoming requests, communicates with models (repositories) to handle data, and returns responses to the client (usually in JSON format) using annotations like `@RestController` and `@RequestMapping`.

### API Documentation
- The API documentation is available via [Swagger UI](http://localhost:8080/api/docs/swagger-ui/index.html) once the application is running. This tool allows you to explore all available endpoints and test them directly from your browser, making it easier to interact with the API.
   ```
   http://localhost:8080/api/docs/swagger-ui/index.html
   ```

## API Endpoints

### Book Routes

| **Endpoint**                   | **Method** | **Description**                                                  |
|--------------------------------|------------|------------------------------------------------------------------|
| `/api/books/count`             | `GET`      | Gets the total count of books in the library.                    |
| `/api/books/random/{count}`    | `GET`      | Gets a random selection of books based on the specified count.   |
| `/api/books/location`          | `GET`      | Gets books organized by their location in the library.           |
| `/api/books/{sortBy}`          | `GET`      | Gets all books sorted by a specific field (e.g., title, author, publisher). |
| `/api/books/search/{keyword}`  | `GET`      | Searches for books by a keyword in titles, authors, publishers, publication year, and ISBN. |
| `/api/books/isbn/{isbn}`       | `GET`      | Gets detailed information for a specific book by its ISBN.       |
| `/api/books`                   | `POST`     | Adds a new book to the library.                                  |
| `/api/books/{isbn}`            | `PUT`      | Updates the information of a specific book by its ISBN.         |
| `/api/books/{isbn}`            | `DELETE`   | Deletes a specific book from the library.                        |

### Authentication Routes

| **Endpoint**                   | **Method** | **Description**                                                  |
|--------------------------------|------------|------------------------------------------------------------------|
| `/api/auth/login`              | `POST`     | Authenticates a user and returns a JWT token.                    |
| `/api/auth/me`                 | `GET`      | Gets the details of the currently authenticated user.           |

### Cover Routes

| **Endpoint**                   | **Method** | **Description**                                                  |
|--------------------------------|------------|------------------------------------------------------------------|
| `/api/covers/{filename}`       | `GET`      | Allows retrieving the cover of a book by its file name.         |

## Author

This backend application for **BIBLIOTECA** has been developed and is maintained by:

**William Medina**

Thank you for reviewing **BIBLIOTECA**!