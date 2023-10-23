# ClassManagement


![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-%2300f.svg?style=for-the-badge&logo=mysql&logoColor=white)&nbsp;<br><br>
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
GET /students - .

GET /students/{registration} - 

POST /student -

GET /student/search - 

POST /auth/login - 

POST /auth/register - 

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

