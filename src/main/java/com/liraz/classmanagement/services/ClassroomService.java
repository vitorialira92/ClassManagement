package com.liraz.classmanagement.services;

import com.liraz.classmanagement.domain.classroom.ClassroomStatus;
import com.liraz.classmanagement.domain.semester.Semester;
import com.liraz.classmanagement.domain.semester.SemesterStatus;
import com.liraz.classmanagement.domain.student.Student;
import com.liraz.classmanagement.domain.student_classes.StudentClass;
import com.liraz.classmanagement.dtos.classroom.ClassroomDTO;
import com.liraz.classmanagement.repositories.ClassroomRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.liraz.classmanagement.domain.classroom.Classroom;

import java.time.LocalDate;
import java.util.ArrayList;
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

    public boolean isPossibleToCreateClassToSemester(String semesterCode){
        return semesterService.getSemesterStatus(semesterCode) == SemesterStatus.PLANNED;
    }

    public Classroom createClass(ClassroomDTO classroomDTO) {

        if(findByCode(classroomDTO.getCode()) == null){

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

        return null;
    }

    public Classroom deleteClass(String code) {
        Classroom classroom = classroomRepository.findByCode(code);

        if(classroom != null){
            classroom.setStatus(ClassroomStatus.CANCELED);
            classroomRepository.save(classroom);
        }
        return classroom;
    }


    public Classroom update(String code, ClassroomDTO classroomDTO) {

        Classroom classroom = findByCode(code);

        if(classroom != null){

            classroom.setEnrolled(classroomDTO.getEnrolled());
            classroom.setName(classroomDTO.getName());
            classroom.setProfessor(classroomDTO.getProfessor());
            classroom.setHoursWeek(classroomDTO.getHoursWeek());
            classroom.setSeats(classroomDTO.getSeats());
            classroom.setSemester(semesterService.findByCode(classroomDTO.getSemesterCode()));

            classroomRepository.save(classroom);

            return classroom;
        }
        return null;
    }

    public List<Classroom> getAllClassrooms() { return classroomRepository.findAll(); }

    public List<Classroom> getASemesterClasses(String semesterCode){
        return classroomRepository.getASemesterClasses(semesterCode);
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
