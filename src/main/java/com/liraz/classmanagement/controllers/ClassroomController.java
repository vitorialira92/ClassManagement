package com.liraz.classmanagement.controllers;

import com.liraz.classmanagement.domain.classroom.Classroom;
import com.liraz.classmanagement.dtos.classroom.ClassroomDTO;
import com.liraz.classmanagement.exceptions.CustomizedException;
import com.liraz.classmanagement.services.ClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{semesterCode}") //ok
    public ResponseEntity<?> getASemesterClasses(@PathVariable String semesterCode){
        List<Classroom> list = classroomService.getASemesterClasses(semesterCode);

        return (list == null)
                ? new ResponseEntity<CustomizedException>(
                        new CustomizedException("No classroom was registered to this semester."),
                            HttpStatus.BAD_REQUEST)
                : (new ResponseEntity<List<Classroom>>(list, HttpStatus.OK));
    }

    @PostMapping //ok
    public ResponseEntity<?> createClass(@RequestBody ClassroomDTO classroomDTO){

        if(classroomService.findByCode(classroomDTO.getCode()) != null){
            new ResponseEntity<CustomizedException>(
                    new CustomizedException("Classroom code already registered."),
                    HttpStatus.BAD_REQUEST);
        }

        Classroom newClass = classroomService.createClass(classroomDTO);

        return (newClass == null)
                ? (ResponseEntity.badRequest().build())
                : (new ResponseEntity<Classroom>(newClass,HttpStatus.CREATED));

    }

    @PutMapping(value = "/{code}") //ok
    public ResponseEntity<?> updateClass(@PathVariable String code,
                                         @RequestBody ClassroomDTO classroomDTO){
        if(classroomService.findByCode(code) == null){
            new ResponseEntity<CustomizedException>(
                    new CustomizedException("There is no classroom registered with this code."),
                    HttpStatus.BAD_REQUEST);
        }

        Classroom classroom = classroomService.update(code, classroomDTO);

        return (classroom == null)
                ? (ResponseEntity.badRequest().build())
                : (new ResponseEntity<Classroom>(classroom,HttpStatus.OK));
    }

    @DeleteMapping(value = "/{code}") //ok
    public ResponseEntity<?> delete(@PathVariable String code){

        if(classroomService.findByCode(code) == null){
            new ResponseEntity<CustomizedException>(
                    new CustomizedException("There is no classroom registered with this code."),
                    HttpStatus.BAD_REQUEST);
        }

        Classroom classroom = classroomService.deleteClass(code);

        return (classroom == null)
                ? (ResponseEntity.badRequest().build())
                : (new ResponseEntity<Classroom>(classroom,HttpStatus.OK));
    }

}
