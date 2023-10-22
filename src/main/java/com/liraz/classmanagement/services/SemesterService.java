package com.liraz.classmanagement.services;

import com.liraz.classmanagement.domain.Semester;
import com.liraz.classmanagement.dtos.semester.SemesterDTO;
import com.liraz.classmanagement.repositories.SemesterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SemesterService {

    @Autowired
    private SemesterRepository repository;

    public Semester createSemester(SemesterDTO semesterDTO){
        if(!semesterExists(semesterDTO.getSemesterCode())){
            Semester semester = new Semester();
            semester.setSemesterCode(semesterDTO.getSemesterCode());
            semester.setSemesterStart(semesterDTO.getSemesterStart());
            semester.setSemesterEnd(semesterDTO.getSemesterEnd());
            semester.setRegistrationEnd(semesterDTO.getRegistrationEnd());
            semester.setRegistrationStart(semesterDTO.getRegistrationStart());
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

        repository.save(semester);
        return semester;
    }

    public Semester deleteSemester(String code){
        Semester semester = repository.findBySemesterCode(code);

        if(semester != null)
            repository.delete(semester);

        return semester;
    }
    private boolean semesterExists(String code){
        return repository.findBySemesterCode(code) != null;
    }

}
