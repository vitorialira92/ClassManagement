package com.liraz.classmanagement.controllers;

import com.liraz.classmanagement.domain.classroom.Classroom;
import com.liraz.classmanagement.domain.semester.Semester;
import com.liraz.classmanagement.domain.student.Student;
import com.liraz.classmanagement.domain.student_classes.StudentClass;
import com.liraz.classmanagement.dtos.classroom.ClassroomDTO;
import com.liraz.classmanagement.exceptions.CustomizedException;
import com.liraz.classmanagement.exceptions.NotFoundException;
import com.liraz.classmanagement.services.ClassroomService;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Management of classrooms")
@RestController
@RequestMapping("/classroom")
public class ClassroomController {
    @Autowired
    private ClassroomService classroomService;

    @Operation(summary = "Get information on a classroom",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Classroom found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Classroom.class))),
                    @ApiResponse(responseCode = "400",
                            description = "Classroom isn't registered",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CustomizedException.class)))
            })
    @GetMapping(value = "/{code}") // ok
    public ResponseEntity<Classroom> findByCode(@Parameter(description = "Classroom code",
            example = "1568A") @PathVariable String code){

        Classroom classroom = classroomService.findByCode(code);

        return new ResponseEntity<Classroom>(classroom,HttpStatus.OK);
    }

    @Operation(summary = "Get information on all classrooms",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "All classrooms",
                            content = @Content(mediaType = "application/json",
                                   array = @ArraySchema(schema = @Schema(implementation = Classroom.class))))
            })
    @GetMapping //ok
    public List<Classroom> getAllClassrooms(){ return classroomService.getAllClassrooms(); }

    @Operation(summary = "Get information on all classrooms in a semester",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "All classrooms in a semester",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Classroom.class)))),
                    @ApiResponse(responseCode = "404",
                            description = "No classroom was registered to this semester",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = NotFoundException.class)))
            })
    @GetMapping("/semester/{semesterCode}") //ok
    public ResponseEntity<?> getASemesterClasses(@Parameter(description =
            "Code of the semester whose classes you want to fetch", example = "2024.2A")
                                                     @PathVariable String semesterCode){
        List<Classroom> list = classroomService.getASemesterClasses(semesterCode);

        return new ResponseEntity<List<Classroom>>(list, HttpStatus.OK);
    }

    @Operation(summary = "Create a classroom",
            responses = {
                    @ApiResponse(responseCode = "201",
                            description = "Classroom created",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Classroom.class))),
                    @ApiResponse(responseCode = "400",
                            description = "You can only register a classroom to a semester " +
                                    "that haven't started yet or Classroom code already registered",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CustomizedException.class))),
            })
    @PostMapping //ok
    public ResponseEntity<Classroom> createClass(@RequestBody ClassroomDTO classroomDTO){

        Classroom newClass = classroomService.createClass(classroomDTO);

        return new ResponseEntity<Classroom>(newClass,HttpStatus.CREATED);

    }

    @Operation(summary = "Update a classroom",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Classroom updated",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Classroom.class))),
                    @ApiResponse(responseCode = "404",
                            description = "There is no classroom registered with this code",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = NotFoundException.class))),
                    @ApiResponse(responseCode = "400",
                            description = "You can not change the semester a classroom is in",
                            content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = CustomizedException.class)))
            })
    @PutMapping(value = "/{code}") //ok
    public ResponseEntity<?> updateClass(@Parameter(description = "Classroom code",
            example = "1568A") @PathVariable String code,
                                         @RequestBody ClassroomDTO classroomDTO){

        Classroom classroom = classroomService.update(code, classroomDTO);

        return new ResponseEntity<Classroom>(classroom,HttpStatus.OK);
    }

    @Operation(summary = "Update a classroom",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Classroom deleted",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Classroom.class))),
                    @ApiResponse(responseCode = "404",
                            description = "There is no classroom registered with this code",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = NotFoundException.class))),
                    @ApiResponse(responseCode = "400",
                            description = "You can not cancel a already finished semester",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CustomizedException.class)))
            })
    @DeleteMapping(value = "/{code}") //ok
    public ResponseEntity<Classroom> delete(@Parameter(description = "Classroom code",
            example = "1568A") @PathVariable String code){

        Classroom classroom = classroomService.deleteClass(code);

        return new ResponseEntity<Classroom>(classroom,HttpStatus.OK);
    }



}
