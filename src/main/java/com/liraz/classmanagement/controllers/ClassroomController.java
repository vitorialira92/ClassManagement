package com.liraz.classmanagement.controllers;

import com.liraz.classmanagement.domain.classroom.Classroom;
import com.liraz.classmanagement.dtos.classroom.ClassroomDTO;
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


    /*@ApiOperation(value = "Returns a classroom according to the classroom's code")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 400, message = ""),
                    @ApiResponse(code = 401, message = "")
            }
    )*/
    @GetMapping(value = "/{code}")
    public ResponseEntity<?> findByCode(@PathVariable String code){

        Classroom classroom = classroomService.findByCode(code);

        return (classroom == null)
                ? (ResponseEntity.badRequest().build())
                : (new ResponseEntity<Classroom>(classroom,HttpStatus.OK));
    }

    @GetMapping
    public ResponseEntity<List<Classroom>> getAllClassrooms(){
        return new ResponseEntity<List<Classroom>>
                (classroomService.getAllClassrooms(),
                    HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<?> createClass(@RequestBody ClassroomDTO classroomDTO){

        Classroom newClass = classroomService.createClass(classroomDTO);

        return (newClass == null)
                ? (ResponseEntity.badRequest().build())
                : (new ResponseEntity<Classroom>(newClass,HttpStatus.CREATED));

    }

    @PutMapping(value = "/{code}")
    public ResponseEntity<?> updateClass(@PathVariable String code,
                                         @RequestBody ClassroomDTO classroomDTO){
        Classroom classroom = classroomService.update(classroomDTO);

        return (classroom == null)
                ? (ResponseEntity.badRequest().build())
                : (new ResponseEntity<Classroom>(classroom,HttpStatus.OK));
    }

    @DeleteMapping(value = "/{code}")
    public ResponseEntity<?> deleteAdmin(@PathVariable String code){
        Classroom newClass = classroomService.deleteClass(code);

        return (newClass == null)
                ? (ResponseEntity.badRequest().build())
                : (new ResponseEntity<Classroom>(newClass,HttpStatus.OK));
    }

}
