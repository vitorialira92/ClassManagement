package com.liraz.classmanagement.controllers;

import com.liraz.classmanagement.domain.Semester;
import com.liraz.classmanagement.dtos.semester.SemesterDTO;
import com.liraz.classmanagement.services.SemesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/semester")
public class SemesterController {

    @Autowired
    private SemesterService semesterService;

    @GetMapping
    public void getAll(){

    }

    @PostMapping
    public ResponseEntity<?> createSemester(@RequestBody SemesterDTO semesterDTO){

        Semester semester = semesterService.createSemester(semesterDTO);

        return (semester == null)
                ?(ResponseEntity.badRequest().build())
                :(new ResponseEntity<Semester>(semester, HttpStatus.CREATED));

    }

    @PutMapping
    public ResponseEntity<?> updateSemester(@RequestBody SemesterDTO semesterDTO){
        Semester semester = semesterService.updateSemester(semesterDTO);

        return (semester == null)
                ?(ResponseEntity.badRequest().build())
                :(new ResponseEntity<Semester>(semester, HttpStatus.OK));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<?> deleteSemester(@PathVariable String code){

        Semester semester = semesterService.deleteSemester(code);

        return (semester == null)
                ?(ResponseEntity.badRequest().build())
                :(ResponseEntity.ok().build());
    }


}
