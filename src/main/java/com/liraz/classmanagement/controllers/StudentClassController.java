package com.liraz.classmanagement.controllers;

import com.liraz.classmanagement.domain.classroom.Classroom;
import com.liraz.classmanagement.domain.student.Student;
import com.liraz.classmanagement.domain.student_classes.StudentClass;
import com.liraz.classmanagement.domain.student_classes.StudentStatus;
import com.liraz.classmanagement.dtos.EnrollInClassDTO;
import com.liraz.classmanagement.exceptions.CustomizedException;
import com.liraz.classmanagement.services.SemesterService;
import com.liraz.classmanagement.services.StudentClassService;
import com.liraz.classmanagement.services.email.EmailSenderService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student_class")
public class StudentClassController {

    @Autowired
    private StudentClassService service;
    @Autowired
    private EmailSenderService emailSenderService;

    @PostMapping() //OK
    public ResponseEntity<?> enrollInClass(@RequestBody EnrollInClassDTO enrollInClassDTO) throws MessagingException {

        StudentClass studentClass = service.enrollStudentInClass(enrollInClassDTO.getClassCode(),
                enrollInClassDTO.getStudentRegistration(), enrollInClassDTO.getSemester());


        return new ResponseEntity<StudentClass>(studentClass, HttpStatus.CREATED);

    }
    @DeleteMapping //ok
    public ResponseEntity<?> deleteEnrollment(@RequestBody EnrollInClassDTO enrollInClassDTO) throws MessagingException {
       service.removeEnrollment(enrollInClassDTO.getClassCode(),
               enrollInClassDTO.getStudentRegistration());

       return ResponseEntity.noContent().build();
    }

    @GetMapping("/{registration}") //ok
    public ResponseEntity<?> fetchClassesForStudentInCurrentSemester(
            @PathVariable int registration){
        return new ResponseEntity<List<StudentClass>>(
                service.fetchClassesForStudentInCurrentSemester(registration),
                    HttpStatus.OK);
    }

    @GetMapping("/class/{classCode}") //ok
    public ResponseEntity<?> getAllStudentsInAClass(@PathVariable String classCode){
        return new ResponseEntity<List<Student>>(service.getAllStudentsInAClass(classCode),
                HttpStatus.OK);
    }
}
