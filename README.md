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
3. You can access SWAGGER documentation at http://localhost:8080/swagger-ui/index.html#/

## API Endpoints
The API provides the following endpoints:

```markdown
---------AUTHENTICATION-----------

POST /auth/login - login any user.

POST /auth/register/student - register a student's login information.

POST /auth/register/admin - register a new admin.

PUT /auth/update/{login} - update login information of a user.

DELETE /auth/delete - delete a user.

---------STUDENT-----------

GET /student - get all students.

GET /student/{registration} - get a student by registration.

POST /student - register a student.

PUT /student/update - update a student's information

PUT /student/deactivate/{registration} - deactivate a student.

---------CLASSROOM-----------

GET /classroom - get all classrooms.

GET /classroom/{code} - get a classroom by code.

GET /classroom/semester/{code} - get all classes in a semester by semester's code.

POST /classroom - register a classroom.

PUT /classroom/code - update a classroom's information

DELETE /classroom/{code} - cancel a classroom.

---------SEMESTER-----------

GET /semester - get all classrooms.

GET /semester/{code} - get a semester by code.

GET /semester/current - get all current semesters.

POST /semester - register a semester.

PUT /semester - update a semester's information

DELETE /semester/{code} - delete a semester.

---------STUDENT_CLASS-----------

GET /student_class/{registration} - get all classes a student is enrolled in a current semester.

GET /student_class/class/{code} - get a semester by code.

POST /student_class - enroll a student to a class.

DELETE /student_class - delete a students enrollment to a class.

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

