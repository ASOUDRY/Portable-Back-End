# AuthCore

AuthCore is a modular Spring Boot backend built around secure, stateless authentication.  
It provides a reusable auth foundation for other applications using JWT access tokens and hashed, database-backed refresh tokens.

---

## What AuthCore provides

AuthCore isolates the auth layer into a reusable backend core with:

- stateless JWT authentication
- database-backed refresh token persistence
- hashed refresh token storage
- token rotation on refresh
- explicit token deletion on logout
- a structure that can grow into a larger modular architecture

---

## Features

- Stateless JWT authentication
- Database-backed refresh tokens
- Split-token refresh design: `id.rawToken`
- Hashed refresh token storage with BCrypt
- Refresh token rotation
- Explicit refresh token revocation on logout
- Custom refresh token validation logic
- Role-based route protection
- Modular backend structure for reuse in other projects

---

## Tech Stack

- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- Spring OAuth2 Resource Server
- Spring Modulith
- Spring AOP
- PostgreSQL
- BCrypt
- Maven

---

## Authentication Model

AuthCore uses a two-token system:

### 1. Access Token
The access token is a JWT.  
Clients send it in the `Authorization: Bearer <token>` header when calling protected endpoints.

The server validates the token on each request by checking:

- signature integrity
- expiration
- embedded claims

Because the system is stateless, the server does not need to store session data for access tokens.

### 2. Refresh Token
The refresh token is long-lived and stored in the database.

Instead of storing the raw token value directly, AuthCore stores:

- a token `id`
- a BCrypt hash of the raw token
- the related user
- expiration metadata
- revocation state

The client receives the refresh token in split form:

`id.rawToken`

On refresh, the server:

1. splits the token into `id` and raw token parts
2. loads the matching database record by `id`
3. checks expiration
4. checks revocation
5. compares the raw token against the stored BCrypt hash
6. rotates the token if validation succeeds

This design allows refresh tokens to remain revocable and secure even if the database is exposed.

---

## Auth Flow

1. A user logs in with valid credentials.
2. The server returns:
   - a JWT access token
   - a refresh token in the form `id.rawToken`
3. The client uses the access token for protected requests.
4. When the access token expires, the client sends the refresh token to the refresh endpoint.
5. The server validates the refresh token against the stored hashed version.
6. If valid, the server issues:
   - a new access token
   - a newly rotated refresh token
7. On logout, the stored refresh token is deleted.
---

## Route Structure

### Public routes
Routes under:

`/api/public/**`

These do not require authentication.

Typical examples:
- register
- login
- refresh

### Protected routes
Routes under:

`/api/private/**`

These require a valid JWT access token.

### Admin routes
Routes under:

`/api/admin/**`

These require both authentication and the correct role or authority.

---

## Security Notes

- Access tokens are stateless and not stored server-side.
- Refresh tokens are stored in the database.
- Raw refresh tokens are not stored directly.
- Refresh token hashes are verified using BCrypt.
- Refresh tokens can expire.
- Refresh tokens are rotated after successful use.
- Authorization is enforced through Spring Security route rules and JWT claims.

---

## Default Data Model

### User
- `id`
- `username`
- `password` (hashed)

### RefreshToken
- `id` (UUID)
- `token` (hashed)
- `user` (relation)
- `expiresAt`

---

## Example Endpoints

> Some of the potential endpoints of the api. 

### Public
- `POST /api/public/register`  
  Create a new user account.

- `POST /api/public/login`  
  Authenticate a user and return an access token plus a refresh token.

- `POST /api/public/refresh`  
  Exchange a valid refresh token for a new access token and rotated refresh token.

### Private
- `PATCH /api/private/updateUser`  
  Update the authenticated user's account details.

- `DELETE /api/private/logoutUser`  
  Log out the authenticated user by deleting the submitted refresh token.

### Admin
- `GET /api/admin/getAllUsers`  
  Retrieve all users.

- `PATCH /api/admin/adminPromotion/{id}`  
  Promote a user to admin.

- `DELETE /api/admin/deleteUser/{id}`  
  Delete a user by id.

---

## Project Structure

The project is intended to keep authentication concerns separated into focused layers, such as:

- controllers for request handling
- services for auth and token logic
- repositories for persistence
- security configuration for route protection and JWT validation
- entities for user and refresh token storage

As the project grows, this structure can be split more cleanly into dedicated modules for auth, users, security, and shared infrastructure.

---

## Prerequisites

Before running the project, make sure you have:

- Java installed
- Maven installed
- PostgreSQL available
- a database created for the application

---

## Configuration

Create a `secret.properties` file in the same location level expected by the application configuration and add:

```properties
DB_URL=your_database_url
DB_USER=your_database_user
DB_PASS=your_database_password
