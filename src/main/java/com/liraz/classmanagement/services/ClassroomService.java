package com.liraz.classmanagement.services;

import com.liraz.classmanagement.dtos.classroom.ClassroomDTO;
import com.liraz.classmanagement.repositories.ClassroomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.liraz.classmanagement.domain.classroom.Classroom;

import java.util.List;


@Service
public class ClassroomService {

    @Autowired
    private ClassroomRepository classroomRepository;
    @Autowired
    private SemesterService semesterService;

    public Classroom findByCode(String code){
        return classroomRepository.findByCode(code);
    }

    public Classroom createClass(ClassroomDTO classroomDTO) {
        if(!classroomExists(classroomDTO.getCode())){
            Classroom newClassroom = new Classroom();
            newClassroom.setCode(classroomDTO.getCode());
            newClassroom.setEnrolled(classroomDTO.getEnrolled());
            newClassroom.setName(classroomDTO.getName());
            newClassroom.setProfessor(classroomDTO.getProfessor());
            newClassroom.setHoursWeek(classroomDTO.getHoursWeek());
            newClassroom.setSeats(classroomDTO.getSeats());
            newClassroom.setStatus(classroomDTO.getStatus());
            newClassroom.setSemester(semesterService.getByCode(classroomDTO.getSemesterCode()));

            return classroomRepository.save(newClassroom);
        }


        return null;
    }

    public Classroom deleteClass(String code) {
        if(classroomExists(code)){
            Classroom classroom = classroomRepository.findByCode(code);
            classroomRepository.delete(classroom);
            return classroom;
        }
        return null;
    }

    private boolean classroomExists(String code){
        return classroomRepository.findByCode(code) != null;
    }

    public Classroom update(ClassroomDTO classroomDTO) {
        if(classroomExists(classroomDTO.getCode())){
            Classroom classroom = new Classroom();

            classroom.setEnrolled(classroomDTO.getEnrolled());
            classroom.setName(classroomDTO.getName());
            classroom.setProfessor(classroomDTO.getProfessor());
            classroom.setHoursWeek(classroomDTO.getHoursWeek());
            classroom.setSeats(classroomDTO.getSeats());
            classroom.setStatus(classroomDTO.getStatus());
            classroom.setSemester(semesterService.getByCode(classroomDTO.getSemesterCode()));

            return classroom;
        }
        return null;
    }

    public List<Classroom> getAllClassrooms() {
        return classroomRepository.findAll();
    }
}
