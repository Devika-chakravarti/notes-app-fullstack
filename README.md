# Notes App — Full-Stack CRUD Application

**Live App:** https://notes-app-fullstack-zeta.vercel.app

---

## Problem Statement

- People often need a quick, distraction-free way to jot down thoughts, tasks, or reminders without opening a heavyweight note-taking app or relying on physical paper.
- Most beginner full-stack projects stop at "runs on localhost" — they never go through the real engineering steps of connecting a database, securing credentials, and shipping to production.
- The goal of this project was to build a genuinely complete product: a note-taking web app that a user can actually create, edit, and delete notes on, hosted live on the internet, accessible from any device, at any time — not just a local demo.

## Solution

- Built a full-stack web application with a decoupled architecture: a Java/Spring Boot REST API on the backend, a React single-page application on the frontend, and a MySQL database for persistence.
- The backend exposes a clean set of REST endpoints that handle all CRUD (Create, Read, Update, Delete) operations for notes.
- The frontend consumes this API to let users add, view, edit, and delete notes through a responsive, modern interface — no page reloads, no manual database interaction.
- The entire application is deployed to free-tier cloud infrastructure (Render for the backend, Aiven for the database, Vercel for the frontend), meaning it is live, permanent, and requires no payment to run or access.

## Features

- **Create notes** — add a note with a title and content through a simple form.
- **View all notes** — notes render instantly in a responsive card grid as soon as the page loads.
- **Edit notes** — clicking "Edit" pre-fills the form with the existing note's data for quick updates.
- **Delete notes** — includes a confirmation prompt so a note can't be deleted by accident.
- **Automatic timestamps** — every note is stamped with its creation date/time on the backend, with no manual input required.
- **Responsive design** — the layout adapts from a single column on mobile to a multi-column grid on desktop.
- **Dark, glassmorphic UI** — a custom-designed interface with layered translucent cards and soft background glows, built entirely with Tailwind CSS utility classes.
- **Form validation** — the "Add Note" button stays disabled until both the title and content fields are filled in.

## Tech Stack

**Backend**
- **Java 21** — core programming language.
- **Spring Boot 4** — application framework used to build the REST API.
- **Spring Data JPA (Hibernate)** — handles the mapping between Java objects and MySQL database tables (ORM), so no raw SQL had to be written for basic operations.
- **MySQL** — relational database used to persist all note data.
- **Maven** — manages project dependencies and builds the application into a deployable JAR.
- **Lombok** — reduces boilerplate code (getters/setters) in the entity class.

**Frontend**
- **React (with Vite)** — component-based UI library; Vite provides a fast development and build tool.
- **Tailwind CSS** — utility-first CSS framework used to build the entire interface without writing custom CSS files.
- **Axios** — handles all HTTP requests from the frontend to the backend API.

**Infrastructure & Tooling**
- **Docker** — the backend is containerized so it can be built and run consistently on any hosting platform.
- **Render** — hosts the live backend service.
- **Aiven** — hosts the managed, cloud-based MySQL database.
- **Vercel** — hosts and auto-deploys the live frontend on every push to the main branch.
- **Git & GitHub** — version control and source hosting for the entire codebase.
- **Postman** — used throughout development to test and verify every API endpoint before building the frontend.

## Architecture

- The project follows a **layered backend architecture** to keep responsibilities separated and the codebase easy to maintain:
  - **Controller layer** — receives HTTP requests and returns responses (the API surface).
  - **Service layer** — contains the business logic (e.g., finding a note before updating it).
  - **Repository layer** — talks to the MySQL database via Spring Data JPA, with zero manual SQL required for standard operations.
  - **Entity layer** — defines the `Note` object and how it maps to a database table.
- The **frontend and backend are fully decoupled** — React never talks to the database directly. Every piece of data flows through the REST API, which is how real-world production applications are structured.
- **Database credentials are never hardcoded.** Both the local and production environments read the database URL, username, and password from environment variables, keeping secrets out of the source code and out of GitHub.

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/notes` | Fetch all notes |
| GET | `/api/notes/{id}` | Fetch a single note by ID |
| POST | `/api/notes` | Create a new note |
| PUT | `/api/notes/{id}` | Update an existing note |
| DELETE | `/api/notes/{id}` | Delete a note |

## Challenges & What I Learned

- **Debugging a Hibernate dialect mismatch** — an early setup used a deprecated `MySQL8Dialect` class that no longer exists in newer Hibernate versions; learned that modern Hibernate auto-detects the dialect from the connection URL, so the manual override could simply be removed.
- **Fixing a Docker permission error in production** — the first Render deployment failed with `Permission denied` on the Maven wrapper script (`mvnw`) inside the container; resolved by explicitly granting execute permission (`chmod +x mvnw`) inside the Dockerfile.
- **Understanding free-tier hosting trade-offs** — an initial attempt on Railway kept crashing due to strict memory limits on its trial plan; switched to a combination of Render, Aiven, and Vercel, all of which offer genuinely free, non-expiring tiers suited for a personal project like this.
- **Securing database credentials** — replaced hardcoded database passwords with environment variables (`${DB_USERNAME}`, `${DB_PASSWORD}`), so the same codebase works locally and in production without ever exposing secrets in Git history.
- **Configuring CORS for a live frontend** — had to explicitly allow the deployed Vercel URL in the backend's CORS configuration once the frontend and backend were both live on different domains, not just `localhost`.

## Author

**Devika Chakravarti**
GitHub: [@Devika-chakravarti](https://github.com/Devika-chakravarti)
