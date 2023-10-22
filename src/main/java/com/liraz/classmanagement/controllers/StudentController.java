package com.liraz.classmanagement.controllers;


import com.liraz.classmanagement.domain.student.Student;
import com.liraz.classmanagement.domain.student.StudentClass;
import com.liraz.classmanagement.dtos.student.StudentClassDTO;
import com.liraz.classmanagement.dtos.student.StudentRequestDTO;
import com.liraz.classmanagement.services.StudentService;
import com.liraz.classmanagement.services.emails.EmailSenderService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private EmailSenderService emailSender;

    String emailTemplate;

    //student entity related
    @GetMapping(value = "/{registration}")
    public ResponseEntity<?> getStudent(@PathVariable int registration){
        Student student = studentService.findByRegistration(registration);

        return (student == null)
                ? (ResponseEntity.notFound().build())
                : (new ResponseEntity<Student>(student, HttpStatus.OK));
    }

    @PostMapping()
    public ResponseEntity<?> createStudent(@RequestBody StudentRequestDTO studentRequestDTO) throws MessagingException {
        Student student = studentService.createStudent(studentRequestDTO);

        if(student == null)
            return ResponseEntity.badRequest().build();

        emailSender.sendRegistrationEmail(student.getEmail(), student.getFirstName());
        return (student == null)
                ?(ResponseEntity.badRequest().build())
                : (new ResponseEntity<Student>(student, HttpStatus.CREATED));

    }

    @PutMapping(value = "/{registration}")
    public ResponseEntity<?> updateStudent(@PathVariable int registration,
                                         @RequestBody StudentRequestDTO studentRequestDTO){
        Student student = studentService.update(studentRequestDTO);

        return (student == null)
                ? (ResponseEntity.badRequest().build())
                : (new ResponseEntity<Student>(student,HttpStatus.OK));
    }
    @DeleteMapping(value = "/{registration}")
    public ResponseEntity<?> deleteAdmin(@PathVariable int registration){

        Student student = studentService.deleteStudent(registration);

        return (student == null)
                ? (ResponseEntity.badRequest().build())
                : (new ResponseEntity<Student>(student, HttpStatus.OK));
    }

    //student's classes related
    @PostMapping(value = "/{registration}")
    public ResponseEntity<?> registerToClass(@PathVariable int registration,
                                             @RequestBody StudentClassDTO studentClassDTO){
        StudentClass studentClass = studentService.registerToClass(studentClassDTO);
        return (studentClass == null)
                ? (ResponseEntity.badRequest().build())
                : (new ResponseEntity<StudentClass>(studentClass, HttpStatus.CREATED));
    }

    @DeleteMapping(value = "/{registration}/{classCode}")
    public ResponseEntity<?> deleteRegistrationToClass(
            @PathVariable int registration,
            @PathVariable String classCode){
        StudentClass studentClass = studentService
                .deleteRegistrationToClass(registration, classCode);
        return (studentClass == null)
                ? (ResponseEntity.badRequest().build())
                : (new ResponseEntity<StudentClass>(studentClass, HttpStatus.OK));
    }



}
