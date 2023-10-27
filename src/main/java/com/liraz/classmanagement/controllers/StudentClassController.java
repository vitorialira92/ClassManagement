package com.liraz.classmanagement.controllers;

import com.liraz.classmanagement.domain.student.Student;
import com.liraz.classmanagement.domain.student_classes.StudentClass;
import com.liraz.classmanagement.dtos.student_class.ClassEndDTO;
import com.liraz.classmanagement.dtos.student_class.EnrollInClassDTO;
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

    @GetMapping("/current/{registration}") //ok
    public ResponseEntity<?> fetchClassesForStudentInCurrentSemester(
            @PathVariable int registration){
        return new ResponseEntity<List<StudentClass>>(
                service.fetchClassesForStudentInCurrentSemester(registration),
                    HttpStatus.OK);
    }

    @GetMapping("/in_registration/{registration}") //ok
    public ResponseEntity<?> fetchClassesForStudentInRegistrationPeriod(
            @PathVariable int registration){
        return new ResponseEntity<List<StudentClass>>(
                service.fetchClassesForStudentInRegistrationPeriod(registration),
                HttpStatus.OK);
    }

    @GetMapping("/class/{classCode}") //ok
    public ResponseEntity<?> getAllStudentsInAClass(@PathVariable String classCode){
        return new ResponseEntity<List<Student>>(service.getAllStudentsInAClass(classCode),
                HttpStatus.OK);
    }

    @PostMapping("/final_results")
    public ResponseEntity<?> setFinalResults(@RequestBody ClassEndDTO classEndDTO){
        service.setFinalResults(classEndDTO);
        return ResponseEntity.ok().build();
    }

}
