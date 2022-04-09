# Basic Blog API
## Written in Java with Spring Boot

Swagger documentation is available at `/swagger-ui/`

## Features
- [x] Authentication
- [x] Posts
- [x] Comments
- [x] Categories
- [x] Search

## Endpoints:
| Endpoint                 | Method | Description                   |
|:-------------------------|:-------|:------------------------------|
| `/auth/login`            | POST   | Login                         |
| `/auth/register`         | POST   | Register                      |
| `/auth/whoami`           | GET    | Get current user              |
|                          |        |                               |
| `/posts`                 | GET    | Get all posts                 |
| `/posts`                 | POST   | Create a post                 |
| `/posts/{id}`            | GET    | Get a post                    |
| `/posts/{id}`            | PUT    | Update a post                 |
| `/posts/{id}`            | DELETE | Delete a post                 |
| `/posts/{id}/comments`   | GET    | Get all comments for a post   |
| `/posts/{id}/comments`   | POST   | Create a comment for a post   |
|                          |        |                               |
| `/categories`            | GET    | Get all categories            |
| `/categories`            | POST   | Create a category(Admin only) |
| `/categories/{id}`       | GET    | Get a category                |
| `/categories/{id}`       | PUT    | Update a category             |
| `/categories/{id}`       | DELETE | Delete a category             |
| `/categories/{id}/posts` | GET    | Get all posts for a category  |
|                          |        |                               |
| `/comments/{id}`         | GET    | Get a comment                 |
| `/comments/{id}`         | PUT    | Update a comment              |
| `/comments/{id}`         | DELETE | Delete a comment              |


