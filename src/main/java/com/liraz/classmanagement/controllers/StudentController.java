package com.liraz.classmanagement.controllers;


import com.liraz.classmanagement.domain.student.Student;
import com.liraz.classmanagement.domain.student_classes.StudentClass;
import com.liraz.classmanagement.dtos.student.StudentClassDTO;
import com.liraz.classmanagement.dtos.student.StudentRegisterDTO;
import com.liraz.classmanagement.dtos.student.StudentRequestDTO;
import com.liraz.classmanagement.exceptions.CustomizedException;
import com.liraz.classmanagement.services.StudentService;
import com.liraz.classmanagement.services.emails.EmailSenderService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

        if(studentService.findByCpf(studentRegisterDTO.getCpf()) != null){
            return new ResponseEntity<CustomizedException>(
                    new CustomizedException(
                            "Student already registered."),
                    HttpStatus.BAD_REQUEST);
        }

        Student student = studentService.createStudent(studentRegisterDTO);

        if(student == null)
            return ResponseEntity.badRequest().build();

        emailService.sendRegistrationEmail(student.getEmail(), student.getFirstName());

        return new ResponseEntity<Student>(student, HttpStatus.CREATED);

    }

    @PutMapping(value = "/{registration}") //ok
    public ResponseEntity<?> updateStudent(@PathVariable int registration,
                                         @RequestBody StudentRequestDTO studentRequestDTO){

        if(studentService.findByCpf(studentRequestDTO.getCpf()) != null){
            return new ResponseEntity<CustomizedException>(
                    new CustomizedException(
                            "Student isn't registered."),
                    HttpStatus.BAD_REQUEST);
        }

        Student student = studentService.update(studentRequestDTO);

        return (student == null)
                ? (ResponseEntity.badRequest().build())
                : (new ResponseEntity<Student>(student,HttpStatus.OK));
    }

    @PutMapping(value = "/deactivate/{registration}") //ok
    public ResponseEntity<?> deactivateStudent(@PathVariable int registration){
        return (studentService.deactivateStudent(registration))
                ? ResponseEntity.ok().build()
                : (ResponseEntity.badRequest().build());
    }




}
