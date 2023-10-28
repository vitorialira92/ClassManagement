package com.liraz.classmanagement.services;

import com.liraz.classmanagement.domain.classroom.ClassroomStatus;
import com.liraz.classmanagement.domain.semester.Semester;
import com.liraz.classmanagement.domain.semester.SemesterStatus;
import com.liraz.classmanagement.dtos.classroom.ClassroomDTO;
import com.liraz.classmanagement.exceptions.CustomizedException;
import com.liraz.classmanagement.exceptions.NotFoundException;
import com.liraz.classmanagement.repositories.ClassroomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.liraz.classmanagement.domain.classroom.Classroom;

import java.time.LocalDate;
import java.util.List;


@Service
public class ClassroomService {

    @Autowired
    private ClassroomRepository classroomRepository;
    @Autowired
    private SemesterService semesterService;

    public Classroom findByCode(String code){
        Classroom classroom =  classroomRepository.findByCode(code);
        if(classroom == null)
            throw new CustomizedException("Classroom isn't registered");
        return classroom;
    }

    public boolean isPossibleToCreateClassToSemester(String semesterCode){
        return semesterService.getSemesterStatus(semesterCode) == SemesterStatus.PLANNED;
    }

    public Classroom createClass(ClassroomDTO classroomDTO) {

        if(!this.isPossibleToCreateClassToSemester(classroomDTO.getSemesterCode())){
           throw new CustomizedException("You can only register a classroom to a semester " +
                            "that haven't started yet.");
        }

        if(this.findByCode(classroomDTO.getCode()) != null){
           throw new CustomizedException("Classroom code already registered.");
        }

        Classroom newClassroom = new Classroom();
        newClassroom.setCode(classroomDTO.getCode());
        newClassroom.setEnrolled(classroomDTO.getEnrolled());
        newClassroom.setName(classroomDTO.getName());
        newClassroom.setProfessor(classroomDTO.getProfessor());
        newClassroom.setHoursWeek(classroomDTO.getHoursWeek());
        newClassroom.setSeats(classroomDTO.getSeats());
        if(semesterService.getSemesterStatus(classroomDTO.getSemesterCode()) == SemesterStatus.PLANNED){
            newClassroom.setStatus(ClassroomStatus.REGISTRATION);
        }else if(semesterService.getSemesterStatus(classroomDTO.getSemesterCode() )== SemesterStatus.ONGOING){
            newClassroom.setStatus(ClassroomStatus.ONGOING);
        }else{
            newClassroom.setStatus(ClassroomStatus.FINISHED);
        }
        newClassroom.setSemester(semesterService.findByCode(classroomDTO.getSemesterCode()));

        return classroomRepository.save(newClassroom);

    }

    public Classroom deleteClass(String code) {
        Classroom classroom = classroomRepository.findByCode(code);

        if(classroom.getStatus() == ClassroomStatus.FINISHED)
            throw new CustomizedException("You can not cancel an already finished semester");

        classroom.setStatus(ClassroomStatus.CANCELED);
        classroomRepository.save(classroom);

        return classroom;
    }


    public Classroom update(String code, ClassroomDTO classroomDTO) {

        Classroom classroom = findByCode(code);

        if(classroom.getSemester() != semesterService.findByCode(classroomDTO.getSemesterCode()))
            throw new CustomizedException("You can not change the semester a classroom is in");

        classroom.setEnrolled(classroomDTO.getEnrolled());
        classroom.setName(classroomDTO.getName());
        classroom.setProfessor(classroomDTO.getProfessor());
        classroom.setHoursWeek(classroomDTO.getHoursWeek());
        classroom.setSeats(classroomDTO.getSeats());
        classroom.setSemester(semesterService.findByCode(classroomDTO.getSemesterCode()));

        classroomRepository.save(classroom);

        return classroom;

    }

    public List<Classroom> getAllClassrooms() { return classroomRepository.findAll(); }

    public List<Classroom> getASemesterClasses(String semesterCode){
        List<Classroom> classes = classroomRepository.getASemesterClasses(semesterCode);

        if(classes == null)
            throw new NotFoundException("No classroom was registered to this semester.");

        return classes;
    }

    public void enrollStudentInClass(String classCode){
       classroomRepository.enrollStudentInClass(classCode);
    }

    public int getEnrolledInClass(String classCode){
        return classroomRepository.getEnrolledInClass(classCode);
    }

    public ClassroomStatus getClassStatus(String classCode) {
        return findByCode(classCode).getStatus();
    }

    public void removeEnrollment(String classCode) {
        classroomRepository.removeEnrollment(classCode);
    }


    @Scheduled(cron = "0 0 0 * * ?")
    public void changeClassroomStatus(){

        List<Classroom> allClasses = classroomRepository.findAll();
        for(Classroom classroom : allClasses){
            Semester semester = classroom.getSemester();
            if(semester.getRegistrationEnd().isBefore(LocalDate.now())
                    && semester.getSemesterEnd().isAfter(LocalDate.now())
                        && classroom.getStatus() == ClassroomStatus.REGISTRATION){
                classroom.setStatus(ClassroomStatus.ONGOING);
                updateStatus(classroom);
            }
            else if(semester.getSemesterEnd().isBefore(LocalDate.now())
                    && classroom.getStatus() == ClassroomStatus.ONGOING){
                classroom.setStatus(ClassroomStatus.FINISHED);
                updateStatus(classroom);
            }
        }

    }

    private void updateStatus(Classroom classroom){
        classroomRepository.save(classroom);
    }
}
