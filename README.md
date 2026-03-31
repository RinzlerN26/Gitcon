# 🚀 GitCon

GitCon is a API-based project that demonstrates how to interact with GitHub-like resources such as repositories, issues, commits, and pull requests using REST APIs. It showcases common HTTP methods like **GET** and **POST** along with example endpoints and request bodies.

---

## ⚙️ Setup & Run

Follow these steps to run the project locally:

### Set GitHub Token (Required)

You need to set your GitHub Personal Access Token as an environment variable.

#### 👉 On PowerShell (Windows)

```

$env:GITHUB_TOKEN="your_github_token_here"

```

#### 👉 On macOS/Linux

```

export GITHUB_TOKEN="your_github_token_here"

```

Use the Maven wrapper to start the Spring Boot application:

```

./mvnw spring-boot:run

```

---

## 📌 Features

- Fetch repositories
- Create issues
- Retrieve issues
- Get commit history
- Create pull requests
- RESTful API design with clear examples

---

## 🛠️ API Endpoints

### 🔹 Get Repositories

**GET** `/api/github/repos`

Fetch a list of repositories.

#### Example Request

```

GET https://template.postman-echo.com/api/github/repos?id=1

```

#### Response

- `200 OK` on success
- Returns repository data (JSON or HTML)

---

### 🔹 Create Issue

**POST** `/api/github/issues`

Create a new issue in a repository.

#### Example Request

```

POST https://template.postman-echo.com/api/github/issues

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

### 🔹 Get Issues

**GET** `/api/github/{owner}/{repo}/issues`

Fetch all issues for a given repository.

#### Example Request

```
GET https://template.postman-echo.com/api/github/RinzlerN26/GitCon/issues
```

---

### 🔹 Get Commits

**GET** `/api/github/{owner}/{repo}/commits`

Retrieve commit history for a repository.

#### Example Request

```
GET https://template.postman-echo.com/api/github/RinzlerN26/GitCon/commits
```

---

### 🔹 Create Pull Request

**POST** `/api/github/{owner}/{repo}/pulls`

Create a new pull request.

#### Example Request

```
POST https://template.postman-echo.com/api/github/RinzlerN26/GitCon/pulls
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

## 🧪 Testing

You can test these APIs using:

- Postman
- cURL
- Any REST client

---

## ⚙️ Tech Stack

- Backend: Spring Boot (assumed)
- API Testing: Postman
- Data Format: JSON

---

## 📖 Notes

- GET requests do not require a request body.
- POST requests require a JSON body for creating resources.
- Ensure proper endpoint paths with correct `owner` and `repo`.
