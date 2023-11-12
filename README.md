# ClassManagement


![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-%2300f.svg?style=for-the-badge&logo=mysql&logoColor=white)&nbsp;<br><br>
This project is an API built using **Java, Java Spring Boot, MySQL as the database, Java Mail Sender for e-mail sending, Swagger for documentation, and Spring Security for authentication control.**

The API simulates the functionality of a college system for the management of classes and students. An admin must register a student's information and, right after, the student will get an email with their registration number, which will be their login, and then they can go and create a password. After that, they can enroll to any class that is in a semester on registration period. They might be denied to enroll in a class, the reasons can be: the class is already full or the registration period is over. They can also delete a registration to a class if the semester is on registration period, if not, they can drop the class. They can also update their non-crucial information, see classes available for enrollment and see all the classes they are enrolled in.

Every time they enroll in a class or delete their enrollment in a class, they get an email informing. Also, once the registration period for a semester ends, all students that are enrolled in a class this semester will get an email with information on the classes they are enrolled in. Once they are deactivated, they can no longer enroll to classes.

An admin should maintain import information in the system. They are responsible for inserting new students to the system, deactivating a student, changing a student status once the semester ends, creating new semesters, creating new classrooms and updating them, if needed.

## Table of Contents

- [Usage](#usage)
- [API Endpoints](#api-endpoints)
- [Authentication](#authentication)
- [Database](#database)
- [Project in development](#project-still-in-development)


## Usage

1. Start the application with Maven
2. The API will be accessible at http://localhost:8080
3. You can access Swagger documentation at http://localhost:8080/swagger-ui/index.html#/

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

GET /student_class/current/{registration} - get all classes a student is enrolled in a current semester.

GET /student_class/in_registration/{registration} - get all classes a student enrolled in a semester on registration period.

GET /student_class/class/{classCode} - get all students in a class.

GET /student_class/results/{classCode} - get the status of all students in a class (PASSED, FAILED, DROPPED).

POST /student_class - enroll a student to a class.

POST /student_class/results - set status of all students (PASSED, FAILED) in a class.

DELETE /student_class - delete a students enrollment in a class or drop a class depending on the moment of the semester.

```

## Authentication
The API uses Spring Security for authentication control. The following roles are available:

```
ROLE_USER - Standard user role for logged-in students.
ROLE_ADMIN - Admin role for managing the whole system.
```
To access protected endpoints as an ADMIN user, provide the appropriate authentication credentials in the request header.

## Database
The project utilizes [MySQL](https://www.mysql.com) as the database.


## Project still in development

This project is still under development and new features might be added later on.


## Upcoming features

- Student status change
- Semester dates verification before allowing it to be created
