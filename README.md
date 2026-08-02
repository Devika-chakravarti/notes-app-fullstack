# Notes App : Full-Stack CRUD Application

A full-stack note-taking web application built with **Spring Boot**, **React**, and **MySQL** — designed as a complete, production-deployed project rather than a "runs on localhost" demo. This was built as part of my project-based learning journey toward becoming a **Java Full Stack Developer (Spring Boot + React)**.

**🔗 Live App:** [notes-app-fullstack-zeta.vercel.app](https://notes-app-fullstack-zeta.vercel.app)
**🔗 Backend API:** Hosted on Render (Dockerized Spring Boot service)
**🔗 Database:** Managed MySQL on Aiven

---

## 📌 Table of Contents

- [Problem Statement](#problem-statement)
- [Solution](#solution)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Project Structure](#project-structure)
- [API Endpoints](#api-endpoints)
- [Testing](#testing)
- [Deployment](#deployment)
- [What I Learned](#what-i-learned)
- [Challenges & Solutions](#challenges--solutions)
- [Known Limitations & Future Improvements](#known-limitations--future-improvements)
- [Author](#author)

---

## Problem Statement

- People often need a quick, distraction-free way to jot down thoughts, tasks, or reminders — without opening a heavyweight note-taking app or relying on physical paper.
- Most beginner full-stack projects stop at "it runs on my machine" — they never go through the real engineering steps of connecting a managed database, securing credentials, containerizing a backend, and shipping to production.
- The goal of this project was to build a genuinely complete product: a note-taking web app that a user can actually create, edit, and delete notes on, hosted live on the internet, accessible from any device, at any time.

## Solution

Built a full-stack web application with a **decoupled architecture**:
- A **Java/Spring Boot REST API** on the backend, following a clean 4-layer architecture (Controller → Service → Repository → Entity).
- A **React (Vite) single-page application** on the frontend, styled entirely with Tailwind CSS.
- A **MySQL database** for persistence, hosted as a managed service on Aiven.
- The frontend never talks to the database directly — every piece of data flows through the REST API, exactly how real-world production applications are structured.
- The entire application is deployed to free-tier cloud infrastructure (**Render** for the backend, **Aiven** for the database, **Vercel** for the frontend) — live, permanent, and requiring no payment to run or access.

## Features

- **Create notes** — add a note with a title and content through a simple form.
- **View all notes** — notes render instantly in a responsive card grid as soon as the page loads.
- **Edit notes** — clicking "Edit" pre-fills the form with the existing note's data for quick updates.
- **Delete notes** — includes a confirmation prompt so a note can't be deleted by accident.
- **Automatic timestamps** — every note is stamped with its creation date/time on the backend, with no manual input required.
- **Responsive design** — the layout adapts from a single column on mobile to a multi-column grid on desktop.
- **Dark, glassmorphic UI** — a custom-designed interface with layered translucent cards and soft background glows, built entirely with Tailwind CSS utility classes.
- **Form validation** — both frontend (disabled submit button) and backend (`@NotBlank`, `@Size` annotations) validation, so the "Add Note" button stays disabled until both fields are filled, and the API rejects invalid data regardless.

---

## Tech Stack

**Backend**
- **Java 21** — core programming language.
- **Spring Boot 4** — application framework used to build the REST API.
- **Spring Data JPA (Hibernate)** — handles the mapping between Java objects and MySQL database tables (ORM), so no raw SQL had to be written for basic operations.
- **MySQL** — relational database used to persist all note data.
- **Maven** — manages project dependencies and builds the application into a deployable JAR.
- **Lombok** — reduces boilerplate code (getters/setters) in the entity class.
- **Jakarta Bean Validation** (`@NotBlank`, `@Size`, `@Valid`) — server-side input validation.

**Frontend**
- **React 19 (with Vite)** — component-based UI library; Vite provides a fast development and build tool.
- **Tailwind CSS v4** — utility-first CSS framework used to build the entire interface without writing custom CSS files.
- **Axios** — handles all HTTP requests from the frontend to the backend API.

**Infrastructure & Tooling**
- **Docker** (multi-stage build) — the backend is containerized so it builds and runs consistently on any hosting platform, while keeping the final image lightweight.
- **Render** — hosts the live backend service.
- **Aiven** — hosts the managed, cloud-based MySQL database.
- **Vercel** — hosts and auto-deploys the live frontend on every push to the main branch.
- **Git & GitHub** — version control and source hosting for the entire codebase.
- **Postman** — used throughout development to test and verify every API endpoint before building the frontend.
- **JUnit 5, Mockito, MockMvc** — backend testing (unit + integration).
- **ESLint** — frontend static code analysis and code quality enforcement.

---

## Architecture

The project follows a **layered backend architecture** to keep responsibilities separated and the codebase easy to maintain:

```
HTTP Request
     │
     ▼
┌─────────────┐     receives requests, returns responses
│  Controller  │     (the API surface — maps HTTP verbs to methods)
└──────┬──────┘
       │
       ▼
┌─────────────┐     business logic lives here
│   Service    │     (e.g., finding a note before updating it,
└──────┬──────┘      throwing errors when a note doesn't exist)
       │
       ▼
┌─────────────┐     talks to MySQL via Spring Data JPA
│  Repository  │     (zero manual SQL for standard operations)
└──────┬──────┘
       │
       ▼
┌─────────────┐     defines the Note object and its
│    Entity    │     mapping to a database table
└─────────────┘
```

**Key architectural decisions:**
- The **frontend and backend are fully decoupled** — React never talks to the database directly. Every piece of data flows through the REST API.
- **Database credentials are never hardcoded.** Both local and production environments read the database URL, username, and password from environment variables, keeping secrets out of the source code and out of GitHub history.
- **CORS is explicitly whitelisted** — only `localhost:5173` (local dev) and the live Vercel URL are allowed to call the backend, rather than allowing all origins (`*`).

---

## Project Structure

```
notes-app-fullstack/
│
├── backend/
│   ├── src/main/java/com/notesapp/notesapp/
│   │   ├── controller/       → NoteController.java (HTTP layer)
│   │   ├── entity/            → Note.java (data model)
│   │   ├── repository/        → NoteRepository.java (data access)
│   │   ├── service/            → NoteService.java (business logic)
│   │   └── NotesAppApplication.java  (entry point)
│   ├── src/main/resources/
│   │   └── application.properties   (env-variable based config)
│   ├── src/test/java/         → unit + integration tests
│   ├── Dockerfile             (multi-stage build)
│   └── pom.xml                (Maven dependencies)
│
└── frontend/
    ├── src/
    │   ├── App.jsx             (entire UI + CRUD logic)
    │   ├── main.jsx             (React entry point)
    │   └── index.css            (Tailwind import)
    ├── index.html
    ├── vite.config.js
    └── package.json
```

---

## API Endpoints

Base URL: `/api/notes`

| Method | Endpoint          | Description                | Request Body        |
| ------ | ----------------- | --------------------------- | -------------------- |
| GET    | `/api/notes`      | Fetch all notes             | —                     |
| GET    | `/api/notes/{id}` | Fetch a single note by ID   | —                     |
| POST   | `/api/notes`      | Create a new note           | `{ title, content }` |
| PUT    | `/api/notes/{id}` | Update an existing note     | `{ title, content }` |
| DELETE | `/api/notes/{id}` | Delete a note                | —                     |

All create/update requests are validated server-side (`@Valid`) — a blank title/content or a title over 100 characters is rejected before it reaches the database.

---

## Testing

The backend includes both **unit tests** and **integration tests**, reflecting two different testing philosophies:

| | `NoteServiceTest` | `NoteControllerTest` |
|---|---|---|
| Annotation | `@SpringBootTest` | `@WebMvcTest` |
| What loads | Full application context (real DB) | Only the web layer |
| Dependencies | Real | Mocked (`@MockitoBean`) |
| Type | Integration test | Unit test |
| Verifies | Business logic + DB behavior | HTTP layer in isolation |

- `NoteServiceTest` exercises the service layer against a real database connection, covering both happy paths (create, read, update, delete) and failure paths (fetching a non-existent ID correctly throws an exception).
- `NoteControllerTest` uses `MockMvc` and a mocked `NoteService` to verify that HTTP requests are routed correctly, status codes are returned as expected, and JSON responses are shaped correctly — without touching a real database.
- A basic Spring Boot smoke test (`contextLoads`) confirms the entire application context boots successfully, catching configuration errors early.

---

## Deployment

Three separate services work together in production:

```
User Browser
     │
     ▼
Vercel (Frontend — React static build, CDN-served)
     │  Axios API calls
     ▼
Render (Backend — Dockerized Spring Boot container)
     │  JDBC connection (via environment variables)
     ▼
Aiven (Managed MySQL Database)
```

- **Render** builds and runs the backend from the provided `Dockerfile` (multi-stage build — compiled with the JDK, run on the lighter JRE image). Database credentials are injected via Render's Environment Variables dashboard, never committed to source control.
- **Aiven** provides a managed MySQL instance with SSL-required connections, handling backups and maintenance.
- **Vercel** auto-deploys the frontend on every push to `main`, detecting the Vite project automatically and building via `npm run build`.

---

## What I Learned

This project was built to deeply understand full-stack fundamentals, not just to produce a working app. Key concepts learned along the way:

**Full-Stack Architecture**
- Why frontend and backend are separate applications communicating via APIs, and why the frontend should never talk to a database directly.
- The purpose of a layered backend architecture (Entity → Repository → Service → Controller) and how it isolates change — e.g., swapping MySQL for PostgreSQL would only touch the Repository layer.

**Spring Boot / Backend**
- How `@Entity`, `@Id`, `@GeneratedValue`, and `@PrePersist` map a Java class to a database table with auto-incrementing IDs and automatic timestamps.
- How Spring Data JPA's `JpaRepository` provides 15–20 CRUD methods with zero SQL, via interface inheritance and generics.
- Dependency Injection (`@Autowired`) and why it makes code loosely coupled and testable.
- How `@RestController`, `@RequestMapping`, `@GetMapping`/`@PostMapping`/etc., `@RequestBody`, and `@PathVariable` together form the REST API surface.
- Why `@CrossOrigin` is required once frontend and backend live on different domains, and the security reasoning behind CORS.
- The trade-offs of `spring.jpa.hibernate.ddl-auto=update` — convenient in development, risky in production without migration tools like Flyway/Liquibase.

**Database (MySQL)**
- How Hibernate's Auto-DDL feature generates tables directly from entity classes during early development.
- Why `String` fields default to `VARCHAR(255)` and when `@Column(columnDefinition = "TEXT")` is necessary for longer content.
- The value of verifying data directly in MySQL Workbench rather than trusting API responses alone.
- What connection pooling (HikariCP) does and why reusing connections matters for performance.

**REST API Design**
- The GET/POST/PUT/DELETE convention and why it makes APIs predictable for any developer.
- The importance of proper HTTP status codes (200, 201, 404, 500) beyond just returning data.
- Testing backend endpoints independently with Postman before building the frontend, to isolate backend vs. frontend issues.

**React / Frontend**
- How `useState` triggers re-renders, and how `useEffect` with an empty dependency array runs logic exactly once on mount.
- Conditional rendering to handle loading, empty, and populated states cleanly.
- Controlled inputs and how they keep form state in sync with the UI in real time.
- Building a single form that handles both "create" and "edit" modes using one piece of state (`editingId`).

**Tooling**
- Node.js/npm fundamentals and how Vite offers a faster alternative to Create React App.
- Tailwind's utility-first approach vs. traditional separate CSS files.
- Why Axios is commonly preferred over the native `fetch()` API for cleaner syntax and error handling.

**Security Fundamentals**
- Why credentials must never be hardcoded, and how environment variables solve this both locally and in production.
- How CORS whitelisting protects an API from being called by arbitrary origins.

**Deployment & DevOps**
- What deployment actually means — moving from `localhost` to a live, 24/7-accessible cloud service.
- Why database, backend, and frontend are typically deployed as three separate services.
- The trade-offs between free-tier hosting platforms (trial-credit models vs. genuinely free tiers with their own limitations, like cold starts).
- Docker fundamentals — what a `Dockerfile` does, and why a **multi-stage build** (compile in one stage, run from a lighter image in the next) keeps the final deployable image small and fast.
- Linux file permission issues when scripts (like `mvnw`) are copied into a container without execute permissions.
- Why monorepo structures need an explicit "root directory" setting so hosting platforms know which folder to build.
- Why CORS configuration must be updated with production URLs once both frontend and backend are live on separate domains.
- How auto-deploy pipelines (GitHub → Render/Vercel) eliminate manual redeployment.

**Git & Version Control**
- Core Git workflow: `init`, `add`, `commit`, `push`, `status`, `log`.
- What `.gitignore` does — and importantly, that it only affects *future* files, not files already committed.
- Using monorepo structure (`backend/` and `frontend/` in one repository) for a small, easily manageable project.

**Debugging**
- A systematic approach: read the error → understand what it means → find the root cause (not just the symptom) → fix → verify.
- Specific real issues solved during this project: a deprecated Hibernate dialect class, a Docker permission error on `mvnw`, CORS misconfiguration after going live, and a memory-limited hosting platform causing repeated backend crashes.

---

## Challenges & Solutions

| Challenge | Root Cause | Solution |
|---|---|---|
| **Hibernate dialect mismatch** | An early setup used a deprecated `MySQL8Dialect` class no longer present in newer Hibernate versions | Removed the manual override — modern Hibernate auto-detects the dialect from the connection URL |
| **Docker permission error in production** | Render's first deployment failed with `Permission denied` on `mvnw` inside the container | Explicitly added `chmod +x mvnw` inside the Dockerfile before running the build |
| **Free-tier hosting instability** | An initial attempt on Railway kept crashing due to strict memory limits (512MB) on its trial plan | Switched to Render + Aiven + Vercel — genuinely free, non-expiring tiers suited for a personal project |
| **Securing database credentials** | Hardcoded passwords are a security risk if the repo is public | Replaced with environment variables (`${DB_USERNAME}`, `${DB_PASSWORD}`) — same codebase works locally and in production without exposing secrets |
| **CORS blocking the live frontend** | Backend only allowed `localhost` by default | Explicitly whitelisted the deployed Vercel URL in the backend's `@CrossOrigin` configuration |

---

## Known Limitations & Future Improvements

Being transparent about what a production-grade version of this project would additionally need:

- **HTTP status codes aren't explicit** — endpoints currently return implicit `200 OK`; `createNote` should return `201 Created` via `ResponseEntity`.
- **No centralized exception handling** — a missing note currently surfaces as a `500 Internal Server Error` instead of a proper `404 Not Found`. Would add a custom exception class + `@ControllerAdvice`.
- **Field injection over constructor injection** — `@Autowired` on fields works, but constructor injection with `final` fields is the more testable, modern convention.
- **Tests run against a real database** — a dedicated in-memory database (H2) would isolate tests from production/dev data.
- **`ddl-auto=update` in use** — fine for a personal project, but production systems should use `validate` with a migration tool like Flyway or Liquibase.
- **Frontend API URL is hardcoded** — should be read from `import.meta.env.VITE_API_URL` so the backend URL can change without a code edit and redeploy.
- **No authentication/authorization** — currently anyone can create, edit, or delete any note. A logical next step would be Spring Security + JWT-based auth with per-user notes.
- **No CI/CD pipeline** — tests aren't automatically run on every push; a GitHub Actions workflow would close this gap.

---

## Author

**Devika Chakravarti**
GitHub: [@Devika-chakravarti](https://github.com/Devika-chakravarti)

*Built as part of a project-based learning journey into Java Full Stack Development (Spring Boot + React).*
