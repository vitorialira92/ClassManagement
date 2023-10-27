package com.liraz.classmanagement.controllers;


import com.liraz.classmanagement.domain.student.Student;
import com.liraz.classmanagement.dtos.student.StudentRegisterDTO;
import com.liraz.classmanagement.dtos.student.StudentRequestDTO;
import com.liraz.classmanagement.exceptions.CustomizedException;
import com.liraz.classmanagement.services.StudentService;
import com.liraz.classmanagement.services.email.EmailSenderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Tag(name = "Management of student")
@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private EmailSenderService emailService;


    @Operation(summary = "Get all the information on a student")
    @GetMapping(value = "/{registration}") //ok
    public ResponseEntity<?> getStudent(@Parameter(description = "Student registration number",
            example = "1058") @PathVariable int registration){

        Student student = studentService.findByRegistration(registration);

        return (student == null)
                ? (ResponseEntity.notFound().build())
                : (new ResponseEntity<Student>(student, HttpStatus.OK));
    }

    @Operation(summary = "Get a list of the information on every student registered")
    @GetMapping //ok
    public List<Student> getAllStudents(){
        return studentService.findAll();
    }

    @Operation(summary = "Register a student", description = "Registers a student, " +
            "enable the creation of their login information and send " +
            "them an email with the notice of registration and their registration number.")
    @PostMapping() //ok
    public ResponseEntity<?> createStudent(@RequestBody StudentRegisterDTO studentRegisterDTO) throws MessagingException {

        Student student = studentService.createStudent(studentRegisterDTO);

        return new ResponseEntity<Student>(student, HttpStatus.CREATED);

    }

    @Operation(summary = "Update a student's information", description = "Update a student's information except " +
            "for CPF and registration number")
    @PutMapping(value = "/update") //ok
    public ResponseEntity<?> updateStudent(@RequestBody StudentRequestDTO studentRequestDTO){

        Student student = studentService.update(studentRequestDTO);

        return new ResponseEntity<Student>(student,HttpStatus.OK);
    }

    @Operation(summary = "Deactivate a student", description = "Deactivate a student so " +
            "that they can no longer enroll to a class.")
    @PutMapping(value = "/deactivate/{registration}") //ok
    public ResponseEntity<?> deactivateStudent(@Parameter(description = "Student registration number",
                                                example = "1056")
                                                   @PathVariable int registration){
        studentService.deactivateStudent(registration);
        return ResponseEntity.ok().build();
    }


}
