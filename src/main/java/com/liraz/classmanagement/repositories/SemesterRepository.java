package com.liraz.classmanagement.repositories;

import com.liraz.classmanagement.domain.semester.Semester;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Repository
public interface SemesterRepository extends JpaRepository<Semester,Long> {
    Semester findBySemesterCode(String semesterCode);
    List<Semester> findAll();

    @Transactional
    @Query("select sm from Semester sm where sm.semesterStatus = 'ONGOING'")
    List<Semester> findCurrentSemesters();
}
