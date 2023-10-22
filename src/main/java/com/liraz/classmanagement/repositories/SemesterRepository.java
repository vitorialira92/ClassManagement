package com.liraz.classmanagement.repositories;

import com.liraz.classmanagement.domain.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SemesterRepository extends JpaRepository<Semester,Long> {
    Semester findBySemesterCode(String semesterCode);
}
