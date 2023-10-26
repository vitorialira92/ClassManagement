package com.liraz.classmanagement.controllers;

import com.liraz.classmanagement.domain.semester.Semester;
import com.liraz.classmanagement.dtos.semester.SemesterDTO;
import com.liraz.classmanagement.exceptions.CustomizedException;
import com.liraz.classmanagement.services.SemesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/semester")
public class SemesterController {

    @Autowired
    private SemesterService semesterService;

    @GetMapping("/{code}")
    public Semester getSemester(@PathVariable String code){ return semesterService.findByCode(code); }

    @GetMapping("/current")
    public List<Semester> getCurrentSemesters(){ return semesterService.getCurrentSemesters(); }

    @GetMapping //ok
    public List<Semester> getAllSemesters(){ return semesterService.findAll(); }

    @PostMapping //ok
    public ResponseEntity<?> createSemester(@RequestBody SemesterDTO semesterDTO){

        Semester semester = semesterService.createSemester(semesterDTO);

        return new ResponseEntity<Semester>(semester, HttpStatus.CREATED);

    }

    @PutMapping //ok
    public ResponseEntity<?> updateSemester(@RequestBody SemesterDTO semesterDTO){

        Semester semester = semesterService.updateSemester(semesterDTO);

        return new ResponseEntity<Semester>(semester, HttpStatus.OK);
    }

    @DeleteMapping("/{code}") //ok
    public ResponseEntity<?> deleteSemester(@PathVariable String code){

        Semester semester = semesterService.deleteSemester(code);

        return ResponseEntity.ok().build();
    }


}
