package com.liraz.classmanagement.controllers;

import com.liraz.classmanagement.domain.semester.Semester;
import com.liraz.classmanagement.domain.student.Student;
import com.liraz.classmanagement.dtos.semester.SemesterDTO;
import com.liraz.classmanagement.exceptions.CustomizedException;
import com.liraz.classmanagement.exceptions.NotFoundException;
import com.liraz.classmanagement.services.SemesterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Management of semesters")
@RestController
@RequestMapping("/semester")
public class SemesterController {

    @Autowired
    private SemesterService semesterService;

    @Operation(summary = "Get information on a semester",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Semester found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Semester.class))),
                    @ApiResponse(responseCode = "404",
                            description = "Semester not found.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = NotFoundException.class)))
            })
    @GetMapping("/{code}")
    public ResponseEntity<Semester> getSemester(@Parameter(description = "Semester code")
                                                    @PathVariable String code){
        return new ResponseEntity<Semester>(semesterService.findByCode(code), HttpStatus.OK);
    }

    @Operation(summary = "Get information on all current semesters",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "All current semesters",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Semester.class))))
            })
    @GetMapping("/current")
    public List<Semester> getCurrentSemesters(){ return semesterService.getCurrentSemesters(); }

    @Operation(summary = "Get information on all semesters",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "All semesters",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Semester.class))))
            })
    @GetMapping //ok
    public List<Semester> getAllSemesters(){ return semesterService.findAll(); }

    @Operation(summary = "Create a new semester",
            responses = {
                    @ApiResponse(responseCode = "201",
                            description = "Semester created",
                            content = @Content(mediaType = "application/json",
                                  schema = @Schema(implementation = Semester.class))),
                    @ApiResponse(responseCode = "400",
                            description = "Semester already registered, " +
                                    "Registration period start must be before its end, " +
                                    "Semester start must be before its end OR " +
                                    "Registration period end must be before semester start ",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CustomizedException.class)))
            })
    @PostMapping //ok
    public ResponseEntity<?> createSemester(@RequestBody SemesterDTO semesterDTO){

        Semester semester = semesterService.createSemester(semesterDTO);

        return new ResponseEntity<Semester>(semester, HttpStatus.CREATED);

    }

    @Operation(summary = "Create a new semester", description = "Update a semester's information, except for its code.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Semester updated",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Semester.class))),
                    @ApiResponse(responseCode = "404", description = "Semester not found.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = NotFoundException.class))),
                    @ApiResponse(responseCode = "400",
                            description = "Semester already registered, " +
                                    "Registration period start must be before its end, " +
                                    "Semester start must be before its end OR " +
                                    "Registration period end must be before semester start ",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CustomizedException.class)))
            })
    @PutMapping //ok
    public ResponseEntity<?> updateSemester(@RequestBody SemesterDTO semesterDTO){

        Semester semester = semesterService.updateSemester(semesterDTO);

        return new ResponseEntity<Semester>(semester, HttpStatus.OK);
    }

    @Operation(summary = "Delete a semester",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Semester deleted",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Semester.class))),
                    @ApiResponse(responseCode = "404", description = "Semester not found.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = NotFoundException.class)))
            })
    @DeleteMapping("/{code}") //ok
    public ResponseEntity<?> deleteSemester(@PathVariable String code){

        Semester semester = semesterService.deleteSemester(code);

        return ResponseEntity.ok().build();
    }


}
