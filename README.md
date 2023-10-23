# ClassManagement


![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-%23316192.svg?style=for-the-badge&logo=mysql&logoColor=white)

This project is an API built using **Java, Java Spring Boot, Flyway Migrations, MySQL as the database, Java Mail Sender for e-mail sending, and Spring Security for authentication control.**

The API simulates the functionality of a college system for the management of classes and students. Users can register themselves into the app, and logged-in students can enroll themselves in a class and cancel their enrollment or drop the class, depending on the date they cancel it. For every change a student does, they will get and email informing whatever change they made. And after enrollment period finishes, they will get and email with all the classes they are enrolled in. Admins can create semesters and classes, and register students. 

## Table of Contents

- [Usage](#usage)
- [API Endpoints](#api-endpoints)
- [Authentication](#authentication)
- [Database](#database)
- [Project in development](#project-still-in-development)


## Usage

1. Start the application with Maven
2. The API will be accessible at http://localhost:8080


## API Endpoints
The API provides the following endpoints:

```markdown
GET /partners - Retrieve a list of all partners.

GET /partners/{partnerId} - Retrieve a specific partner by ID.

POST /partners - Register a new partner (ADMIN access required).

GET /partners/search - Search for partners near a specified location.

POST /auth/login - Login into the App

POST /auth/register - Register a new user into the App

```

## Authentication
The API uses Spring Security for authentication control. The following roles are available:

```
ROLE_USER - Standard user role for logged-in students.
ROLE_ADMIN - Admin role for managing the whole system.
```
To access protected endpoints as an ADMIN user, provide the appropriate authentication credentials in the request header.

## Database
The project utilizes [MySQL](https://www.mysql.com) as the database. The necessary database migrations are managed using Flyway.


## Project still in development

This project is under development!

