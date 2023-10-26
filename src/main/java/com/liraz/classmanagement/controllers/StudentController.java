package com.liraz.classmanagement.controllers;


import com.liraz.classmanagement.domain.student.Student;
import com.liraz.classmanagement.dtos.student.StudentRegisterDTO;
import com.liraz.classmanagement.dtos.student.StudentRequestDTO;
import com.liraz.classmanagement.exceptions.CustomizedException;
import com.liraz.classmanagement.services.StudentService;
import com.liraz.classmanagement.services.email.EmailSenderService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private EmailSenderService emailService;

    private String emailTemplate;

    //student entity related
    @GetMapping(value = "/{registration}") //ok
    public ResponseEntity<?> getStudent(@PathVariable int registration){

        Student student = studentService.findByRegistration(registration);

        return (student == null)
                ? (ResponseEntity.notFound().build())
                : (new ResponseEntity<Student>(student, HttpStatus.OK));
    }

    @GetMapping //ok
    public List<Student> getAllStudents(){
        return studentService.findAll();
    }

    @PostMapping() //ok
    public ResponseEntity<?> createStudent(@RequestBody StudentRegisterDTO studentRegisterDTO) throws MessagingException {

        Student student = studentService.createStudent(studentRegisterDTO);

        return new ResponseEntity<Student>(student, HttpStatus.CREATED);

    }

    @PutMapping(value = "/update") //ok
    public ResponseEntity<?> updateStudent(@RequestBody StudentRequestDTO studentRequestDTO){

        Student student = studentService.update(studentRequestDTO);

        return new ResponseEntity<Student>(student,HttpStatus.OK);
    }

    @PutMapping(value = "/deactivate/{registration}") //ok
    public ResponseEntity<?> deactivateStudent(@PathVariable int registration){
        studentService.deactivateStudent(registration);
        return ResponseEntity.ok().build();
    }


}
