# Notes App — Frontend

A responsive, dark-themed single-page application for creating, editing, and deleting notes — built with React and Tailwind CSS, connected to a Spring Boot REST API.

## Features

- View all notes in a responsive card grid (single column on mobile, multi-column on desktop)
- Create a new note via a simple title + content form
- Edit an existing note — clicking "Edit" pre-fills the form for quick updates
- Delete a note, with a confirmation prompt to prevent accidental deletion
- Frontend form validation — the "Add Note" button stays disabled until both fields are filled
- Dark, glassmorphic UI — layered translucent cards with soft background glows

## Tech Stack

- **React 19 (Vite)** — component-based UI, fast dev/build tooling
- **Tailwind CSS v4** — utility-first styling, no separate CSS files
- **Axios** — handles all HTTP requests to the backend API

## Getting Started

1. Clone the repository
2. Install dependencies:
   ```
   npm install
   ```
3. Make sure the backend is running (see backend README)
4. Start the dev server:
   ```
   npm run dev
   ```
5. Open `http://localhost:5173`

## Project Structure

```
src/
├── App.jsx       → entire UI + CRUD logic
├── main.jsx      → React entry point
└── index.css     → Tailwind import
```

## Environment

By default, the app expects the backend API at `http://localhost:8080/api/notes`. In production, this should be read from an environment variable (`import.meta.env.VITE_API_URL`) rather than hardcoded — see Known Limitations in the backend README.

## Deployment

Hosted on **Vercel**, which auto-deploys on every push to `main` — Vercel detects the Vite project automatically and builds via `npm run build`.

## Known Limitations

- API base URL is currently hardcoded rather than environment-driven.
- All CRUD logic and UI currently live in a single `App.jsx` file rather than being split into smaller components — fine at this scale, but would need restructuring for a larger feature set.
- No loading skeletons — a brief blank state can appear while notes are being fetched.