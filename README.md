# GitCon

GitCon is a full-stack GitHub integration and security analysis platform built with **Spring Boot** and **React + TypeScript**.

It provides REST APIs and a web dashboard for interacting with GitHub resources such as repositories, issues, commits, and pull requests. It also provides AI-powered security analysis for detecting hardcoded secrets in commit content.

---

## Architecture

GitCon consists of two applications:

- **GitCon Backend** — Spring Boot REST API
- **GitCon UI** — React + TypeScript web dashboard

The application uses:

- PostgreSQL for persistence
- Nginx as a reverse proxy in Docker environments
- pgAdmin for database administration
- GitHub API for GitHub integration
- Gemini API for AI-powered security analysis

---

## Project Structure

```text
GitCon/
│
├── Gitcon/                         # Spring Boot backend
│   ├── src/
│   ├── Dockerfile
│   ├── docker-compose.local.yml
│   ├── docker-compose.yml
│   ├── docker-compose.prod.yml
│   ├── nginx/
│   │   ├── nginx.dev.conf
│   │   └── nginx.prod.conf
│   ├── .env
│   └── README.md
│
└── Gitcon-UI/                      # React frontend
    ├── src/
    ├── public/
    ├── Dockerfile
    ├── Dockerfile.dev
    ├── package.json
    ├── vite.config.ts
    └── README.md
```

---

# Features

## GitHub Integration

- Fetch repositories
- Create issues
- Retrieve issues
- Get commit history
- Create pull requests
- Manage GitHub credentials

## Security

- AI-powered secret detection
- Commit-based security scanning
- Detection of hardcoded credentials
- Detection of API keys and tokens
- Security analysis using Gemini

## Web Dashboard

The GitCon UI provides a web interface for:

- GitHub repository interaction
- Issue management
- Commit browsing
- Pull request creation
- Security scanning
- GitHub credential management

---

# Tech Stack

## Backend

- Java 21
- Spring Boot
- Spring Security
- Maven
- PostgreSQL
- JWT
- REST APIs

## Frontend

- React
- TypeScript
- Vite
- Redux Toolkit
- RTK Query
- React Router
- Chakra UI
- Axios
- React Icons

## Infrastructure

- Docker
- Docker Compose
- Nginx
- PostgreSQL
- pgAdmin

## External Services

- GitHub API
- Gemini API

---

# Environment Configuration

Create a `.env` file inside the `Gitcon` backend directory.

```env
DB_HOST=localhost
DB_PORT=5432
DB_NAME=gitcon
DB_USERNAME=gitcon
DB_PASSWORD=gitcon_password

GITHUB_TOKEN=your_github_token_here
GEMINI_API_KEY=your_gemini_api_key_here

JWT_SECRET=your_jwt_secret_here
GITHUB_ENCRYPTION_KEY=your_github_encryption_key_here
```

For Docker environments, the backend connects to PostgreSQL using the Docker service name:

```env
DB_HOST=db-server
DB_PORT=5432
```

> Do not commit `.env` to source control.

---

# Running GitCon

GitCon supports three configurations:

| Configuration | Backend | Frontend     | PostgreSQL | pgAdmin | Nginx    |
| ------------- | ------- | ------------ | ---------- | ------- | -------- |
| Local         | Host    | Host         | Docker     | Docker  | Not used |
| Docker Dev    | Docker  | Docker/Vite  | Docker     | Docker  | Docker   |
| Docker Prod   | Docker  | Docker Build | Docker     | Docker  | Docker   |

---

# 1. Local Development

In the local configuration, only the database infrastructure runs inside Docker.

```text
React/Vite        → Host
Spring Boot       → Host
PostgreSQL        → Docker
pgAdmin           → Docker
Nginx             → Not used
```

## Start PostgreSQL and pgAdmin

From the `Gitcon` backend directory:

```bash
docker compose -f docker-compose.local.yml up -d
```

This starts:

- PostgreSQL
- pgAdmin

### PostgreSQL

```text
Host: localhost
Port: 5432
Database: gitcon
```

### pgAdmin

Open:

```text
http://localhost:5050
```

Default credentials:

```text
Email: admin@gitcon.com
Password: admin
```

---

## Start the Backend

For local execution, configure:

```env
DB_HOST=localhost
DB_PORT=5432
```

Then run:

### macOS/Linux

```bash
./mvnw spring-boot:run
```

### Windows PowerShell

```powershell
.\mvnw spring-boot:run
```

The backend runs at:

```text
http://localhost:8080
```

---

## Start the Frontend

Navigate to:

```text
Gitcon-UI/
```

Install dependencies:

```bash
npm install
```

Start Vite:

```bash
npm run dev
```

The UI runs at:

```text
http://localhost:5173
```

The Vite development proxy forwards:

```text
/api/*
```

to:

```text
http://localhost:8080
```

---

## Local Architecture

```text
                  Browser
                     │
                     ▼
              ┌─────────────┐
              │ Vite :5173  │
              └──────┬──────┘
                     │
                  /api/*
                     │
                     ▼
              ┌─────────────┐
              │Spring :8080 │
              └──────┬──────┘
                     │
                     ▼
             ┌──────────────┐
             │ PostgreSQL   │
             │    :5432     │
             └──────────────┘
                     ▲
                     │
             ┌──────────────┐
             │   pgAdmin    │
             │    :5050     │
             └──────────────┘
```

---

# 2. Docker Development

Docker development runs the complete application inside Docker.

```text
Spring Boot → Docker
React/Vite  → Docker
PostgreSQL  → Docker
pgAdmin     → Docker
Nginx       → Docker
```

Start the environment from the `Gitcon` directory:

```bash
docker compose up --build
```

The application is available at:

```text
http://localhost
```

pgAdmin is available at:

```text
http://localhost:5050
```

---

## Docker Development Architecture

```text
                         Browser
                            │
                            ▼
                     ┌─────────────┐
                     │ Nginx :80   │
                     └──────┬──────┘
                            │
                 ┌──────────┴──────────┐
                 │                     │
                 ▼                     ▼
           Vite UI :5173          Spring :8080
                 │                     │
                 │ /api                │
                 └─────────────────────┘
                                       │
                                       ▼
                                PostgreSQL :5432
                                       ▲
                                       │
                                  pgAdmin :5050
```

### Development UI

The UI runs using the Vite development server.

Docker provides:

```env
VITE_API_PROXY_TARGET=http://spring-server:8080
CHOKIDAR_USEPOLLING=true
```

Vite proxies:

```text
/api/*
```

to:

```text
http://spring-server:8080
```

---

# 3. Docker Production

Production runs the complete application inside Docker using built artifacts.

```text
React → Production Build
Spring Boot → Executable JAR
PostgreSQL → Docker
pgAdmin → Docker
Nginx → Docker
```

Start production:

```bash
docker compose -f docker-compose.prod.yml up --build -d
```

Application:

```text
http://localhost
```

pgAdmin:

```text
http://localhost:5050
```

---

## Production Architecture

```text
                         Browser
                            │
                            ▼
                     ┌─────────────┐
                     │ Nginx :80   │
                     └──────┬──────┘
                            │
                 ┌──────────┴──────────┐
                 │                     │
                 ▼                     ▼
           React static files       /api/*
                                       │
                                       ▼
                                Spring :8080
                                       │
                                       ▼
                                PostgreSQL
                                       ▲
                                       │
                                  pgAdmin
```

In production:

- React is built using Vite
- The generated `dist` directory contains the frontend assets
- Nginx serves the React static files
- Nginx forwards `/api` requests to Spring Boot
- Vite's development server is not used

---

# Nginx Routing

Nginx is used only in the Docker Development and Docker Production configurations.

## Docker Development

```text
/       → Vite :5173
/api/*  → Spring Boot :8080
```

## Docker Production

```text
/       → React static files
/api/*  → Spring Boot :8080
```

The frontend therefore communicates with the backend using:

```text
/api
```

without hardcoding the backend host or port.

---

# Frontend API Configuration

The React application uses:

```env
VITE_API_BASE_URL=/api
```

API requests look like:

```text
/api/github/repos
/api/github/issues
/api/github/{owner}/{repo}/issues
/api/github/{owner}/{repo}/commits
/api/security/scan-secrets
```

Routing is handled by:

- Vite proxy during development
- Nginx during production

---

# API Endpoints

## Get Repositories

```http
GET /api/github/repos
```

Fetch repositories for the configured GitHub account.

---

## Create Issue

```http
POST /api/github/issues
```

Example:

```json
{
  "owner": "RinzlerN26",
  "repo": "GitCon",
  "title": "Test Issue From GitCon",
  "body": "This issue was created via Spring Boot API"
}
```

---

## Get Issues

```http
GET /api/github/{owner}/{repo}/issues
```

Example:

```http
GET /api/github/RinzlerN26/GitCon/issues
```

---

## Get Commits

```http
GET /api/github/{owner}/{repo}/commits
```

Example:

```http
GET /api/github/RinzlerN26/GitCon/commits
```

---

## Scan for Secrets

```http
POST /api/security/scan-secrets
```

Example:

```json
{
  "owner": "octocat",
  "repository": "Hello-World",
  "commitHash": "e4fa4ae5dd1d709ce4168397bd1d200fec1b2494",
  "scanType": "SECRETS"
}
```

The endpoint analyzes commit content using an AI model to identify potential hardcoded secrets.

---

## Create Pull Request

```http
POST /api/github/{owner}/{repo}/pulls
```

Example:

```json
{
  "title": "My First PR",
  "head": "feature_branch",
  "base": "main",
  "body": "This is a test pull request"
}
```

---

# Database

GitCon uses PostgreSQL.

Database configuration is provided through environment variables:

```env
DB_NAME=gitcon
DB_USERNAME=gitcon
DB_PASSWORD=gitcon_password
```

PostgreSQL data is persisted using the Docker volume:

```text
postgres_data
```

The volume is mounted at:

```text
/var/lib/postgresql/data
```

---

# pgAdmin

pgAdmin is available in all three configurations.

```text
http://localhost:5050
```

Default credentials:

```text
Email: admin@gitcon.com
Password: admin
```

---

# Authentication

GitCon uses JWT-based authentication.

The frontend stores the authentication token using:

```text
gitcon_token
```

RTK Query automatically adds the token to API requests:

```http
Authorization: Bearer <token>
```

---

# Security Configuration

The following values must be kept private:

```env
GITHUB_TOKEN=...
GEMINI_API_KEY=...
JWT_SECRET=...
GITHUB_ENCRYPTION_KEY=...
DB_PASSWORD=...
```

These values should only exist in the backend environment configuration.

Do not expose them through frontend `VITE_*` variables.

---

# API Testing

The backend APIs can be tested using:

- Postman
- cURL
- Swagger/OpenAPI
- GitCon UI

---

# GitCon UI

The frontend is maintained as a separate project:

```text
Gitcon-UI/
```

For frontend-specific setup, development, Docker, and build instructions, see:

```text
Gitcon-UI/README.md
```

---

# Useful Docker Commands

## Local

Start infrastructure:

```bash
docker compose -f docker-compose.local.yml up -d
```

Stop infrastructure:

```bash
docker compose -f docker-compose.local.yml down
```

---

## Development

Start:

```bash
docker compose up --build
```

Stop:

```bash
docker compose down
```

---

## Production

Start:

```bash
docker compose -f docker-compose.prod.yml up --build -d
```

Stop:

```bash
docker compose -f docker-compose.prod.yml down
```

View logs:

```bash
docker compose -f docker-compose.prod.yml logs -f
```
