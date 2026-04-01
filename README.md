# GitCon

GitCon demonstrates how to interact with GitHub-like resources such as repositories, issues, commits, and pull requests using REST APIs. It showcases common HTTP methods like **GET** and **POST** along with example endpoints and request bodies.

---

## Setup & Run

## Run with Docker

You can also run the application using Docker instead of running it locally.

### Create `.env` File

Create a `.env` file in the root directory:

```env
GITHUB_TOKEN=your_github_token_here
```

Run the below command:

```
docker compose -f docker-compose.prod.yml up -d
```

## Run Locally (Alternative)

Follow these steps to run the project locally:

### Set GitHub Token (Required)

You need to set your GitHub Personal Access Token as an environment variable.

#### On PowerShell (Windows)

```powershell
$env:GITHUB_TOKEN="your_github_token_here"
```

#### On macOS/Linux

```bash
export GITHUB_TOKEN="your_github_token_here"
```

Use the Maven wrapper to start the Spring Boot application (JDK>=21 should be installed):

```
./mvnw spring-boot:run
```

---

## Features

- Fetch repositories
- Create issues
- Retrieve issues
- Get commit history
- Create pull requests
- RESTful API design with clear examples

---

## API Endpoints

### Get Repositories

**GET** `/api/github/repos`

Fetch a list of repositories.

#### Example Request

```
GET https://localhost:8080/api/github/repos?id=1
```

#### Response

- `200 OK` on success
- Returns repository data (JSON or HTML)

---

### Create Issue

**POST** `/api/github/issues`

Create a new issue in a repository.

#### Example Request

```
POST https://localhost:8080/api/github/issues
```

#### Request Body (JSON)

```json
{
  "owner": "RinzlerN26",
  "repo": "GitCon",
  "title": "Test Issue From GitCon",
  "body": "This issue was created via Spring Boot API"
}
```

#### Response

- `200 OK` or `201 Created` on success
- Returns created issue data

---

### Get Issues

**GET** `/api/github/{owner}/{repo}/issues`

Fetch all issues for a given repository.

#### Example Request

```
GET https://localhost:8080/api/github/RinzlerN26/GitCon/issues
```

---

### Get Commits

**GET** `/api/github/{owner}/{repo}/commits`

Retrieve commit history for a repository.

#### Example Request

```
GET https://localhost:8080/api/github/RinzlerN26/GitCon/commits
```

---

### Create Pull Request

**POST** `/api/github/{owner}/{repo}/pulls`

Create a new pull request.

#### Example Request

```
POST https://localhost:8080/api/github/RinzlerN26/GitCon/pulls
```

#### Request Body (JSON)

```json
{
  "title": "My First PR",
  "head": "feature_branch",
  "base": "main",
  "body": "This is a test pull request"
}
```

#### Response

- `200 OK` or `201 Created`
- Returns pull request details

---

## Testing

You can test these APIs using:

- Postman
- cURL
- Any REST client

---

## Tech Stack

- Backend: Spring Boot
- API Testing: Postman
- Data Format: JSON

---

## Notes

- GET requests do not require a request body.
- POST requests require a JSON body for creating resources.
- Ensure proper endpoint paths with correct `owner` and `repo`.

---

### Screenshots

## Running the application

<img width="1769" height="828" style="border:1px solid #000" alt="image" src="https://github.com/user-attachments/assets/39948c74-fdd0-4d2a-9103-2fb72791c889" />

## Testing Endpoints

<img width="1370" height="820"  style="border:1px solid #000" alt="image" src="https://github.com/user-attachments/assets/06f14b14-6d0d-4373-9b11-431a70012d83" />

<br><br>

<img width="1364" height="868" style="border:1px solid #000" alt="image" src="https://github.com/user-attachments/assets/d6d1e776-b678-48c4-ac1f-d4bc093d9d27" />
