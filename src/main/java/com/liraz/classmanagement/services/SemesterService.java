package com.liraz.classmanagement.services;

import com.liraz.classmanagement.domain.classroom.Classroom;
import com.liraz.classmanagement.domain.classroom.ClassroomStatus;
import com.liraz.classmanagement.domain.semester.Semester;
import com.liraz.classmanagement.domain.semester.SemesterStatus;
import com.liraz.classmanagement.dtos.semester.SemesterDTO;
import com.liraz.classmanagement.repositories.SemesterRepository;
import com.liraz.classmanagement.services.email.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SemesterService {

    @Autowired
    private SemesterRepository repository;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private StudentService studentService;

    public Semester createSemester(SemesterDTO semesterDTO){

        if(repository.findBySemesterCode(semesterDTO.getSemesterCode()) == null){
            Semester semester = new Semester();
            semester.setSemesterCode(semesterDTO.getSemesterCode());
            semester.setSemesterStart(semesterDTO.getSemesterStart());
            semester.setSemesterEnd(semesterDTO.getSemesterEnd());
            semester.setRegistrationEnd(semesterDTO.getRegistrationEnd());
            semester.setRegistrationStart(semesterDTO.getRegistrationStart());
            semester.setSemesterStatus(semesterDTO.getSemesterStatus());

            repository.save(semester);
            return semester;
        }
        return null;
    }

    public Semester updateSemester(SemesterDTO semesterDTO){

        Semester semester = repository.findBySemesterCode(semesterDTO.getSemesterCode());

        if(semester == null)
            return null;

        semester.setSemesterStart(semesterDTO.getSemesterStart());
        semester.setSemesterEnd(semesterDTO.getSemesterEnd());
        semester.setRegistrationEnd(semesterDTO.getRegistrationEnd());
        semester.setRegistrationStart(semesterDTO.getRegistrationStart());
        semester.setSemesterStatus(semesterDTO.getSemesterStatus());

        repository.save(semester);
        return semester;
    }

    public Semester deleteSemester(String code){

        Semester semester = repository.findBySemesterCode(code);

        if(semester != null)
            repository.delete(semester);

        return semester;
    }

    public Semester findByCode(String code){
        return repository.findBySemesterCode(code);
    }
    private boolean semesterExists(String code){
        return repository.findBySemesterCode(code) != null;
    }

    public List<Semester> findAll() {
        return repository.findAll();
    }

    public List<Semester> getCurrentSemesters() {
        return repository.findCurrentSemesters();
    }

    public SemesterStatus getSemesterStatus(String code){
        return findByCode(code).getSemesterStatus();
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void changeSemesterStatus(){

        List<Semester> allSemesters = repository.findAll();

        for(Semester semester : allSemesters){
            if(semester.getRegistrationEnd().isBefore(LocalDate.now())
                    && semester.getSemesterEnd().isAfter(LocalDate.now())){
                semester.setSemesterStatus(SemesterStatus.ONGOING);
                updateStatus(semester);
            }
            else if(semester.getSemesterEnd().isBefore(LocalDate.now())){
                semester.setSemesterStatus(SemesterStatus.CONCLUDED);
                updateStatus(semester);
            }
        }

    }

    private void updateStatus(Semester semester){
        repository.save(semester);
    }
}
