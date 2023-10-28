package com.liraz.classmanagement.controllers;

import com.liraz.classmanagement.domain.student.Student;
import com.liraz.classmanagement.domain.student_classes.StudentClass;
import com.liraz.classmanagement.dtos.student_class.ClassRecordDTO;
import com.liraz.classmanagement.dtos.student_class.EnrollInClassDTO;
import com.liraz.classmanagement.exceptions.CustomizedException;
import com.liraz.classmanagement.exceptions.NotFoundException;
import com.liraz.classmanagement.services.StudentClassService;
import com.liraz.classmanagement.services.email.EmailSenderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Management of class enrollment")
@RestController
@RequestMapping("/student_class")
public class StudentClassController {

    @Autowired
    private StudentClassService service;
    @Autowired
    private EmailSenderService emailSenderService;

    @Operation(summary = "Enroll a student in a class.",
                description = "Enroll a student in a class, if the semester is on " +
                        "REGISTRATION period and the student is active.", responses = {
            @ApiResponse(responseCode = "201",
                    description = "Student enrollment",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StudentClass.class))),
            @ApiResponse(responseCode = "400",
                    description = "Student already enrolled in this class, " +
                            " \"It is not possible to enroll to this classroom. It is not on the registration period\"" +
                            " or Student registration deactivated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomizedException.class)))
    })
    @PostMapping() //OK
    public ResponseEntity<?> enrollInClass(@RequestBody EnrollInClassDTO enrollInClassDTO) throws MessagingException {

        StudentClass studentClass = service.enrollStudentInClass(enrollInClassDTO.getClassCode(),
                enrollInClassDTO.getStudentRegistration(), enrollInClassDTO.getSemester());


        return new ResponseEntity<StudentClass>(studentClass, HttpStatus.CREATED);

    }
    @Operation(summary = "Remove enrollment in a class or drop the class, depending on the period of the semester.",
            description = "Remove enrollment in a class if the semester is on REGISTRATION period." +
                    "Drop the class if the semester is ONGOING.", responses = {
            @ApiResponse(responseCode = "204",
                    description = "Student's enrollment deleted or student dropped the class"),
            @ApiResponse(responseCode = "404",
                    description = "Student isn't enrolled in this class",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NotFoundException.class)))
    })
    @DeleteMapping //ok
    public ResponseEntity<?> deleteEnrollment(@RequestBody EnrollInClassDTO enrollInClassDTO) throws MessagingException {
       service.removeEnrollment(enrollInClassDTO.getClassCode(),
               enrollInClassDTO.getStudentRegistration());

       return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all classes a student is enrolled in for the ongoing semesters.", responses = {
            @ApiResponse(responseCode = "200",
                    description = "A student's all enrollments",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = StudentClass.class))))
    })
    @GetMapping("/current/{registration}")
    public ResponseEntity<?> fetchClassesForStudentInCurrentSemester(
            @Parameter(description = "Registration number of the student whose classes you want to fetch.")
                @PathVariable int registration){
        return new ResponseEntity<List<StudentClass>>(
                service.fetchClassesForStudentInCurrentSemester(registration),
                    HttpStatus.OK);
    }

    @Operation(summary = "Get all classes a student is enrolled in for the semesters on registration period.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "A student's all classes on registration period",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema =
                                    @Schema(implementation = StudentClass.class))))
    })
    @GetMapping("/in_registration/{registration}") //ok
    public ResponseEntity<?> fetchClassesForStudentInRegistrationPeriod(
            @Parameter(description = "Registration number of the student whose classes you want to fetch.")
            @PathVariable int registration){
        return new ResponseEntity<List<StudentClass>>(
                service.fetchClassesForStudentInRegistrationPeriod(registration),
                HttpStatus.OK);
    }

    @Operation(summary = "Get all students currently enrolled in a class.", responses = {
            @ApiResponse(responseCode = "200",
                    description = "All students enrolled in a class",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Student.class))))
    })
    @GetMapping("/class/{classCode}") //ok
    public ResponseEntity<?> getAllStudentsInAClass(
            @Parameter(description = "Code of the class whose students you want to fetch.")
            @PathVariable String classCode){
        return new ResponseEntity<List<Student>>(service.getAllStudentsInAClass(classCode),
                HttpStatus.OK);
    }

    @Operation(summary = "Set the status of all students (PASSED, FAILED) in a class.", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Classes results set"),
            @ApiResponse(responseCode = "400",
                    description = "You can not update the status of a student that dropped the class",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomizedException.class))),
            @ApiResponse(responseCode = "404",
                    description = "Classroom not registered or Student isn't enrolled in this class",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NotFoundException.class)))
    })
    @PostMapping("/results")
    public ResponseEntity<?> setClassResults(@RequestBody ClassRecordDTO classRecordDTO){
        service.setClassResults(classRecordDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get the status of all students (PASSED, FAILED, DROPPED) in a class.", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Class results",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClassRecordDTO.class)))
    })
    @GetMapping("/results/{classCode}")
    public ResponseEntity<?> getClassResults(@Parameter(description = "Code of the class whose students' " +
            "status you want to fetch.") @PathVariable String classCode){
        return new ResponseEntity<ClassRecordDTO>(service.getClassResults(classCode), HttpStatus.OK);
    }
}
