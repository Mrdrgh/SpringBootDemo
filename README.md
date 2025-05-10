# Task Management REST API

A secure REST API for collaborative task management built with Spring Boot and JWT authentication.

## Features

- **Secure Authentication**: JWT-based authentication and authorization
- **User Management**: Registration and authentication endpoints
- **Role-Based Access Control**: Different permissions for USER and ADMIN roles
- **Task Management**: CRUD operations for tasks with proper authorization
- **Data Persistence**: H2 in-memory database (easily configurable for production databases)
- **API Documentation**: Interactive API documentation with Swagger/OpenAPI

## Technologies

- Java 17
- Spring Boot 3.4.5
- Spring Security
- Spring Data JPA
- JWT (JSON Web Token)
- H2 Database
- Swagger/OpenAPI for documentation
- Maven for dependency management

## Getting Started

### Prerequisites

- JDK 17 or higher
- Maven

### Running the Application

1. Clone the repository
```bash
git clone https://github.com/Mrdrgh/SpringBootDemo.git
cd SpringBootDemo
```

2. Build and run the application
```bash
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8081`
you can change the port in the ```server.port``` property in (src/main/resources/application.properties)

## API Documentation

This API is documented through this README and the included screenshots showing how to use the endpoints.

## API Endpoints

### Authentication

- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Login and get JWT token

### Tasks

- `GET /api/tasks` - Get all tasks (requires authentication)
- `GET /api/tasks/{id}` - Get task by ID (requires authentication)
- `POST /api/tasks` - Create a new task (requires authentication)
- `PUT /api/tasks/{id}` - Update a task (requires authentication and proper authorization)
- `DELETE /api/tasks/{id}` - Delete a task (requires authentication and proper authorization)

## User Roles

- **USER**: Can create, read, update, and delete their own tasks
- **ADMIN**: Can manage all tasks and all users

## Security

- JWT-based authentication
- Password encryption with BCrypt
- Role-based authorization
- CSRF protection disabled (for API usage)
- CORS enabled for cross-origin requests

## Sample JSON Payloads

### User Registration
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123"
}
```

### Login
```json
{
  "email": "john@example.com",
  "password": "password123"
}
```

### Create Task
```json
{
  "title": "Implement API",
  "description": "Implement a secure REST API using Spring Boot"
}
```

## API in Action

Below are descriptions of the API's functionality:

### User Registration
The API supports user registration with a name, email, and password. A successful registration returns a 201 Created response with a JWT token and user details including:
- token
- email
- name
- role (default is "USER")

### User Login
Users can login with their email and password. A successful login returns a 200 OK response with:
- JWT token for authentication
- User details (email, name, role)

### Creating a Task
Authenticated users can create tasks by providing:
- title: The name of the task
- description: Detailed information about the task

A Bearer Token must be included in the Authorization header. Successful task creation returns a 201 Created status.

### Task Details
When a task is created, the system returns the task with:
- id: Unique identifier for the task
- title: The name of the task
- description: Detailed information
- status: Default status is "À_FAIRE" (To Do)
- assignedUserId: ID of the user who created or is assigned to the task

## Testing

The project includes unit tests for the service layer. Run the tests with:

```bash
./mvnw test
```

## Configuration

Key configuration properties in `application.properties`:

```properties
# Server Configuration
server.port=8081

# H2 Database Configuration
spring.datasource.url=jdbc:h2:mem:tasksdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JWT Configuration
jwt.secret=5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437
jwt.expiration=86400000
```

## H2 Console

The H2 database console is available at:
```
http://localhost:8081/h2-console
```

Use these connection details:
- JDBC URL: `jdbc:h2:mem:tasksdb`
- Username: `sa`
- Password: `password`

## Continuous Integration

This project includes a GitHub Actions workflow for continuous integration.

## Testing with Postman

You can test the API using Postman. Here are examples with screenshots showing how to set up your tests and the responses you should expect:

### User Registration

1. **Register a new user**:
   - Method: POST
   - URL: `http://localhost:8081/api/auth/register`
   - Body (raw, JSON):
   ```json
   {
     "name": "test user",
     "email": "test@test.com",
     "password": "password"
   }
   ```

**Screenshot of registration request and response:**
![User Registration](docs/screenshots/Capture%20d'écran%202025-05-10%20133152.png)

### User Login

2. **Login to get a token**:
   - Method: POST
   - URL: `http://localhost:8081/api/auth/login`
   - Body (raw, JSON):
   ```json
   {
     "email": "test@test.com",
     "password": "password"
   }
   ```
   - Save the token from the response

**Screenshot of login request and response:**
![User Login](docs/screenshots/Capture%20d'écran%202025-05-10%20133222.png)

### Authentication Setup

After login, you need to configure your Bearer Token authentication:

**Screenshot of authentication configuration in Postman:**
![Authentication in Postman](docs/screenshots/Capture%20d'écran%202025-05-10%20133446.png)

### Creating a Task

3. **Create a task** (requires authentication):
   - Method: POST
   - URL: `http://localhost:8081/api/tasks`
   - Headers: Add `Authorization: Bearer YOUR_TOKEN` (replace YOUR_TOKEN with the token from login)
   - Body (raw, JSON):
   ```json
   {
     "title": "Test Task",
     "description": "This is a test task"
   }
   ```

**Screenshot of task creation request:**
![Creating a Task](docs/screenshots/Capture%20d'écran%202025-05-10%20133454.png)

**Screenshot of task creation response showing details:**
![Task Details](docs/screenshots/Capture%20d'écran%202025-05-10%20133446.png)

The API will return appropriate status codes:
- 200 OK for successful GET requests
- 201 Created for successful creation operations
- 401 Unauthorized for invalid or missing authentication
- 403 Forbidden for operations without proper permissions
- 404 Not Found for resources that don't exist
- 500 Internal Server Error for server-side issues

## Author
Darghal Mohammed <mohammed.darghal.23@ump.ac.ma>

## License

This project is licensed under the MIT License - see the LICENSE file for details.
