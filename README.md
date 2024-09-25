
# Spring Session Management Demo

## Project Overview

This project demonstrates advanced concepts of Spring Boot, including session handling, authentication, and controllers that manage session-related functionalities.

### Key Features:
1. **Spring Boot Application** with advanced Spring framework concepts.
2. **Session Handling**: Includes session creation, retrieval, validation, and invalidation.
3. **Authentication Mechanism**: Protects certain routes.
4. **Two Controllers**:
    - **SessionController**: Manages session creation, retrieval, validation, and invalidation.
    - **ProtectedController**: Requires a valid session ID to access its methods.
5. **Security**: Basic Authentication is implemented.

### How to Run:
1. Download and unzip the project.
2. Open the project in Eclipse or IntelliJ.
3. Run the `SpringSessionDemoApplication.java` file as a Spring Boot application.

### Endpoints:
1. **SessionController**:
    - **Create Session**: `POST /session/create`
        - Creates a session and returns the session ID.
        - Example: `curl -X POST http://localhost:8080/session/create -u user:password`
    - **Retrieve Session**: `GET /session/retrieve`
        - Retrieves current session details.
        - Example: `curl http://localhost:8080/session/retrieve?sessionId=<sessionId> -u user:password`
    - **Invalidate Session**: `POST /session/invalidate`
        - Invalidates the session by session ID.
        - Example: `curl -X POST http://localhost:8080/session/invalidate?sessionId=<sessionId> -u user:password`

2. **ProtectedController**:
    - **Access Protected Resource**: `GET /protected/resource`
        - Requires a valid session ID.
        - Example: `curl http://localhost:8080/protected/resource?sessionId=<sessionId> -u user:password`

### Authentication:
- Default Username: `user`
- Default Password: Printed in the console or can be set in `application.properties`.

### Notes:
- The project uses Spring Security for authentication.
- CSRF protection is disabled for testing purposes.
