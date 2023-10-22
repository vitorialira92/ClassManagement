package com.liraz.classmanagement.repositories;

import com.liraz.classmanagement.domain.classroom.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom,Long> {
    Classroom findByCode(String code);
}
