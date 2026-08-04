# Notes App — Backend

REST API for a note-taking application, built with Spring Boot and MySQL, following a clean layered architecture (Controller → Service → Repository → Entity).

## Features

- Full CRUD on notes (create, read, update, delete)
- Automatic timestamps on every note (`@PrePersist`) — no manual input required
- Server-side validation (`@NotBlank`, `@Size`) on title and content
- Zero raw SQL — all standard queries handled via Spring Data JPA's `JpaRepository`

## Tech Stack

- **Java 21**
- **Spring Boot 4** — REST API framework
- **Spring Data JPA (Hibernate)** — ORM, maps `Note` entity to MySQL table
- **MySQL** — relational database
- **Maven** — dependency management and build
- **Lombok** — reduces boilerplate (getters/setters)
- **Jakarta Bean Validation** — `@NotBlank`, `@Size`, `@Valid`

## Getting Started

1. Clone the repository
2. Create a MySQL database (e.g. `notesapp_db`)
3. Set the following environment variables (or update `application.properties` directly for local dev):
   - `DB_URL`
   - `DB_USERNAME`
   - `DB_PASSWORD`
4. Run the app:
   ```
   ./mvnw spring-boot:run
   ```
5. API will be available at `http://localhost:8080/api/notes`

## Project Structure

```
src/main/java/com/notesapp/notesapp/
├── controller/   → NoteController.java (HTTP layer)
├── entity/       → Note.java (data model)
├── repository/   → NoteRepository.java (data access)
├── service/      → NoteService.java (business logic)
└── NotesAppApplication.java (entry point)
```

## API Endpoints

Base URL: `/api/notes`

| Method | Endpoint          | Description                | Request Body        |
|--------|-------------------|-----------------------------|----------------------|
| GET    | `/api/notes`      | Fetch all notes             | —                    |
| GET    | `/api/notes/{id}` | Fetch a single note by ID   | —                    |
| POST   | `/api/notes`      | Create a new note           | `{ title, content }` |
| PUT    | `/api/notes/{id}` | Update an existing note     | `{ title, content }` |
| DELETE | `/api/notes/{id}` | Delete a note                | —                    |

All create/update requests are validated server-side — a blank title/content or a title over 100 characters is rejected before it reaches the database.

## Testing

The backend includes both unit and integration tests:

- **`NoteServiceTest`** (`@SpringBootTest`) — exercises the service layer against a real database connection; covers create, read, update, delete, plus failure paths like fetching a non-existent ID.
- **`NoteControllerTest`** (`@WebMvcTest`) — uses `MockMvc` and a mocked `NoteService` to verify routing, status codes, and JSON response shape, without touching a real database.
- A basic Spring Boot smoke test (`contextLoads`) confirms the application context boots successfully.

Run tests:
```
./mvnw test
```

## Deployment

- Containerized with a multi-stage **Dockerfile** (built with the JDK, run on a lighter JRE image).
- Hosted on **Render**; database credentials are injected via Render's Environment Variables dashboard, never committed to source control.
- Connects to a managed **MySQL instance on Aiven** (SSL-required).
- CORS is explicitly whitelisted to the local dev origin and the deployed frontend URL.

## Known Limitations

- HTTP status codes aren't fully explicit — `createNote` currently returns implicit `200 OK` rather than `201 Created`.
- No centralized exception handling — a missing note currently surfaces as `500 Internal Server Error` instead of `404 Not Found`.
- Uses field injection (`@Autowired` on fields) rather than constructor injection.
- Tests run against a real database rather than an isolated in-memory one (H2).
- `spring.jpa.hibernate.ddl-auto=update` is fine for a personal project, but a production system should use `validate` with a migration tool like Flyway or Liquibase.
- No authentication — anyone can create, edit, or delete any note.