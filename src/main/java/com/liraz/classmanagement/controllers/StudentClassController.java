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

        if(service.checkIfStudentIsEnrolledInClass(enrollInClassDTO.getClassCode(),
                enrollInClassDTO.getStudentRegistration())){
            return new ResponseEntity<CustomizedException>(
                    new CustomizedException(
                            "Student already enrolled in this class."),
                    HttpStatus.BAD_REQUEST);
        }

        if(!service.checkIfClassIsUpForEnrollment(enrollInClassDTO.getClassCode())){
            return new ResponseEntity<CustomizedException>(new CustomizedException(
                    "It is not possible to enroll to this classroom."),
                    HttpStatus.BAD_REQUEST);
        }

        if(!service.studentIsActive(enrollInClassDTO.getStudentRegistration())){
            return new ResponseEntity<CustomizedException>(new CustomizedException(
                    "Student registration deactivated."),
                    HttpStatus.BAD_REQUEST);
        }

        StudentClass studentClass = service.enrollStudentInClass(enrollInClassDTO.getClassCode(),
                enrollInClassDTO.getStudentRegistration(), enrollInClassDTO.getSemester());


        return (studentClass == null)
                ? ResponseEntity.badRequest().build()
                : (new ResponseEntity<StudentClass>(studentClass, HttpStatus.CREATED));

    }
    @DeleteMapping //ok
    public ResponseEntity<?> deleteEnrollment(@RequestBody EnrollInClassDTO enrollInClassDTO) throws MessagingException {
        if(!service.checkIfStudentIsEnrolledInClass(enrollInClassDTO.getClassCode(),
                enrollInClassDTO.getStudentRegistration())){
            return new ResponseEntity<CustomizedException>(
                    new CustomizedException(
                            "Student isn't enrolled in this class."),
                    HttpStatus.BAD_REQUEST);
        }

        if(service.checkIfClassIsUpForEnrollment(enrollInClassDTO.getClassCode())){ // so delete
            service.removeEnrollment(enrollInClassDTO.getClassCode(),
                    enrollInClassDTO.getStudentRegistration());
            return ResponseEntity.ok().build();

        }else{ //change status to dropped
            StudentClass studentClass = service.
                    getEnrollment(enrollInClassDTO.getClassCode(), enrollInClassDTO.getStudentRegistration());
            studentClass.setStudentStatus(StudentStatus.DROPPED);
            return new ResponseEntity<StudentClass>(studentClass, HttpStatus.CREATED);
        }

    }

    @GetMapping("/{registration}") //ok
    public ResponseEntity<?> fetchClassesForStudentInCurrentSemester(
            @PathVariable int registration){
        return new ResponseEntity<List<StudentClass>>(
                service.fetchClassesForStudentInCurrentSemester(registration),
                    HttpStatus.OK);
    }

    @GetMapping("/class/{classCode}") //ok
    public ResponseEntity<?> getAllStudentsIdInAClass(@PathVariable String classCode){
        return new ResponseEntity<List<Student>>(service.getAllStudentsInAClass(classCode),
                HttpStatus.OK);
    }
}
