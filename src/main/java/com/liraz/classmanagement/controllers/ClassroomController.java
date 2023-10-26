package com.liraz.classmanagement.controllers;

import com.liraz.classmanagement.domain.classroom.Classroom;
import com.liraz.classmanagement.domain.student.Student;
import com.liraz.classmanagement.domain.student_classes.StudentClass;
import com.liraz.classmanagement.dtos.classroom.ClassroomDTO;
import com.liraz.classmanagement.exceptions.CustomizedException;
import com.liraz.classmanagement.services.ClassroomService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/classroom")
public class ClassroomController {
    @Autowired
    private ClassroomService classroomService;

    @GetMapping(value = "/{code}") // ok
    public ResponseEntity<?> findByCode(@PathVariable String code){

        Classroom classroom = classroomService.findByCode(code);

        return (classroom == null)
                ? new ResponseEntity<CustomizedException>(
                        new CustomizedException("Classroom isn't registered."), HttpStatus.BAD_REQUEST)
                : (new ResponseEntity<Classroom>(classroom,HttpStatus.OK));
    }

    @GetMapping //ok
    public List<Classroom> getAllClassrooms(){ return classroomService.getAllClassrooms(); }

    @GetMapping("/semester/{semesterCode}") //ok
    public ResponseEntity<?> getASemesterClasses(@PathVariable String semesterCode){
        List<Classroom> list = classroomService.getASemesterClasses(semesterCode);

        return new ResponseEntity<List<Classroom>>(list, HttpStatus.OK);
    }

    @PostMapping //ok
    public ResponseEntity<?> createClass(@RequestBody ClassroomDTO classroomDTO){

        Classroom newClass = classroomService.createClass(classroomDTO);

        return new ResponseEntity<Classroom>(newClass,HttpStatus.CREATED);

    }

    @PutMapping(value = "/{code}") //ok
    public ResponseEntity<?> updateClass(@PathVariable String code,
                                         @RequestBody ClassroomDTO classroomDTO){

        Classroom classroom = classroomService.update(code, classroomDTO);

        return new ResponseEntity<Classroom>(classroom,HttpStatus.OK);
    }

    @DeleteMapping(value = "/{code}") //ok
    public ResponseEntity<?> delete(@PathVariable String code){

        Classroom classroom = classroomService.deleteClass(code);

        return new ResponseEntity<Classroom>(classroom,HttpStatus.OK);
    }



}
