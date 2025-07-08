
[JAVA_BADGE]:https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white
[SPRING_BADGE]: https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white
[AWS_BADGE]:https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white


<h1 align="center" style="font-weight: bold;"> 💻 EasyMenu API </h1>

![AWS][AWS_BADGE]
![spring][SPRING_BADGE]
![java][JAVA_BADGE]

<p align="center">
 <a href="#features">Features</a> • 
 <a href="#started">Getting Started</a> • 
 <a href="#routes">API Endpoints</a> •
 <a href="#version">Versioning</a> •
 <a href="#contribute">Contribute</a> 
</p>

<p align="center">
  <b>A RESTful API for managing products and orders in the food service industry. Built with Spring Boot, using layered architecture, JWT authentication, database migrations, testing, and CI/CD</b>
</p>

<h2 id="features">⚙️ Features</h2>

- 🔐 JWT-based authentication
- 📦 Product & Order CRUD operations
- 🔍 Search filters via DTO
- 💾 Flyway for schema migrations
- 🧪 Unit & integration tests (Mockito + H2)
- 📄 API docs via Swagger/OpenAPI
- 🐳 Docker support
- 🚀 CI/CD pipeline via GitHub Actions
- ⚡ Future improvements: Redis cache

<h2 id="started">🚀 Getting started</h2>

<h3>Prerequisites</h3>

Here you list all prerequisites necessary for running this project:

- [Java 17+](https://www.oracle.com/java/technologies/downloads/)
- [Maven](https://maven.apache.org/)
- [PostgreSQL](https://www.postgresql.org/)
- [Git](https://git-scm.com/)

<h3>Cloning</h3>

How to clone your project

```bash
git clone https://github.com/YOUR_USERNAME/easymenu.git
cd easymenu/backend
```

<h3> Environment Variables</h2>

Copy the file `application-local-example.yml` to `application-local.yml` and update the credentials accordingly:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/easymenu_db
    username: postgres
    password: your_password
api:
  security:
    token:
      secret: your_secret_key
```

<h3>Starting</h3>

In the /backend folder, run the following code:

```bash
./mvnw spring-boot:run
``````

<h3>Access:</h3>

- API base URL: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`


Here you can find the main routes of the API and examples of requests and responses.

| Route                          | Description                                               |
|--------------------------------|-----------------------------------------------------------|
| <kbd>POST /auth/login</kbd>    | Authenticate user and receive JWT token [details](#post-auth-login) |
| <kbd>POST /auth/register</kbd> | User registration [details](#post-user-register) |
| <kbd>PUT /users/{id}</kbd>  | Update user [details](#post-user-deactivate)                |
| <kbd>POST /products</kbd>      | Create a new product [details](#post-product)            |
| <kbd>GET /products/search</kbd>       | Search product by criteria [details](#search-products)               |
| <kbd>GET /orders</kbd>         | Create a new order [details](#post-orders)                   |
| <kbd>POST /orders/search</kbd>        | Search orders by criteria [details](#post-search-orders)                |

<h3 id="post-auth-login">POST /authenticate</h3> 

**REQUEST**
```json
{
    "name": "vinicius",
    "password": "123456789"
}
```

**RESPONSE**
```json
{
    "token": "eyJhbGciOiJIUzI1Ni...",
}
```

<h3 id="post-user-register">POST /auth/register</h3>

**REQUEST**
```json
{
    "name": "vinicius",
    "email": "vinicius@email.com",
    "password": "123456789",
    "role": "USER"
}
```

**RESPONSE**
```json
{
    "id": "0c5f2537-fee8-410b-8fa7-a9f0d59d2218",
    "name": "vinicius",
    "email": "vinicius@email.com",
    "status": "ACTIVE",
    "role": "USER",
    "createdOn": "2025-07-08T08:17:59.030454Z",
    "updatedOn": "2025-07-08T08:17:59.030458Z",
    "_links": {
        "self": {
            "href": "http://localhost:8080/users/0c5f2537-fee8-410b-8fa7-a9f0d59d2218"
        }
    }
}
```
<h3 id="put-user-update">PUT /users/{id}</h3>

**REQUEST**
```json
{
"name": "Vinicius Rodrigues"
}
```
**RESPONSE**
```json
{
    "id": "0c5f2537-fee8-410b-8fa7-a9f0d59d2218",
    "name": "Vinicius Rodrigues",
    "email": "vinicius@email.com",
    "status": "ACTIVE",
    "role": "USER",
    "createdOn": "2025-07-08T08:17:59.030454Z",
    "updatedOn": "2025-07-08T22:12:31.642592Z"
}
```

<h3 id="post-product">POST /products/</h3>

**REQUEST**
```json
{
  "batchId": "123456789",
  "name": "Banana",
  "description": "Fresh banana",
  "price": 1.50,
  "validityStart": "2024-01-01",
  "validityEnd": "2024-12-31"
}
```

**RESPONSE**
```json
{
    "id": "7041469d-6c39-41a9-937c-c4d450a75d05",
    "batchId": 123456789,
    "name": "Banana",
    "description": "Fresh banana",
    "price": 1.50,
    "validityStart": "2024-01-01",
    "validityEnd": "2024-12-31",
    "createdOn": "2025-07-08T08:12:56.861978Z",
    "updatedOn": "2025-07-08T08:12:56.861991Z",
    "_links": {
        "self": {
            "href": "http://localhost:8080/products/7041469d-6c39-41a9-937c-c4d450a75d05"
        }
    }
}
```

<h3 id="search-products">POST /products/search</h3>
<p>One or multiple criteria can be use to search a product, e.g, name, description, range prices or dates. If no data is entered, all users will be returned as output.
</p>

**REQUEST**
```json
{
    "createdOn": "2025-07-08"
}
```

**RESPONSE**
```json
{
    "content": [
        {
            "id": "7041469d-6c39-41a9-937c-c4d450a75d05",
            "batchId": 123456789,
            "name": "Banana",
            "description": "Fresh banana",
            "price": 1.50,
            "validityStart": "2024-01-01",
            "validityEnd": "2024-12-31",
            "createdOn": "2025-07-08T08:12:56.861978Z",
            "updatedOn": "2025-07-08T08:12:56.861991Z",
            "links": [
                {
                    "rel": "self",
                    "href": "http://localhost:8080/products/7041469d-6c39-41a9-937c-c4d450a75d05"
                }
            ]
        }
    ],
    "page": 0,
    "size": 1,
    "totalElements": 1,
    "totalPages": 1,
    "isFirst": true,
    "isLast": true
}
```

<h3 id="post-orders">POST /orders/</h3>

**REQUEST**
```json
{
  "userId": "0c5f2537-fee8-410b-8fa7-a9f0d59d2218",
  "productsId": [
    "7041469d-6c39-41a9-937c-c4d450a75d05",
    "9e0475f5-6025-470c-8490-fc8efdc2135d"
  ],
  "observation": "Deliver before 5 PM"
}

```

**RESPONSE**
```json
{
    "orderId": "e097c978-fb3e-4e4d-ae8e-834178c24e18",
    "orderNumber": 5,
    "user": {
        ...
    },
    "products": [
        {
           ...
        },
        {
           ...
        }
    ],
    "status": "PENDING",
    "totalAmount": 6.00,
    "observation": "Deliver before 5 PM",
    "createdOn": "2025-07-08T08:20:00.911991Z",
    "updatedOn": "2025-07-08T08:20:00.912006Z",
    "_links": {
        "self": {
            "href": "http://localhost:8080/orders/e097c978-fb3e-4e4d-ae8e-834178c24e18"
        }
    }
}
```

<h3 id="post-search-orders">POST /orders/search</h3>
<p>One or multiple criteria can be use to search an order, e.g, id, users, products, range prices or dates. If no data is entered, all orders will be returned as output.
</p>

**REQUEST**
```json
{
  "orderId": "e097c978-f..."
}
```
**RESPONSE**
```json
[
    {
      "content": [
          {
              "orderId": "e097c978-fb3e-4e4d-ae8e-834178c24e18",
              "orderNumber": 5,
              "user": {
                  ...
              },
              "products": [
                  {
                      ...
                  },
                  {
                     ...
                  }
              ],
              "status": "PENDING",
              "totalAmount": 6.00,
              "observation": "Deliver before 5 PM",
              "createdOn": "2025-07-08T08:20:00.911991Z",
              "updatedOn": "2025-07-08T08:20:00.912006Z",
              "links": [
                  {
                      "rel": "self",
                      "href": "http://localhost:8080/orders/e097c978-fb3e-4e4d-ae8e-834178c24e18"
                  }
              ]
          }
      ],
      "page": 0,
      "size": 1,
      "totalElements": 1,
      "totalPages": 1,
      "isFirst": true,
      "isLast": true
  }
]
```

<h2 id="version">📫 Versioning</h2>
| Version                       | Description                                              |
|--------------------------------|-----------------------------------------------------------|
| <kbd>v1.0</kbd>    | Initial release: CRUD, JWT, Swagger, CI/CD |
| <kbd>v1.1</kbd>    | Planned: Add Redis caching |

<h2 id="contribute">📫 Contribute</h2>
<p>I leave here my invitation for you who want to contribute to my project, or if you are a student looking for a project to study by making commits and fixing bugs, know that you are more than welcome to participate. Below I leave some articles that can help you in this process as it helped me.
</p>

1. `git clone https://github.com/Fernanda-Kipper/text-editor.git`
2. `git checkout -b feature/NAME`
3. Follow commit patterns
4. Open a Pull Request explaining the problem solved or feature made, if exists, append screenshot of visual modifications and wait for the review!

<h3>Documentations that might help</h3>

[📝 How to create a Pull Request](https://www.atlassian.com/br/git/tutorials/making-a-pull-request)
[🧪 How to test the Web Layer](https://spring.io/guides/gs/testing-web)
[🧱 Layered Architecture](https://medium.com/java-vault/layered-architecture-b2f4ebe8d587)
[💾 Commit pattern](https://gist.github.com/joshbuchea/6f47e86d2510bce28f8e7f42ae84c716)
