package com.liraz.classmanagement.repositories;

import com.liraz.classmanagement.domain.classroom.Classroom;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom,Long> {
    Classroom findByCode(String code);

    @Transactional
    @Query("select cl from Classroom cl where cl.semester.semesterCode =:semester")
    List<Classroom> getASemesterClasses(@Param("semester") String semesterCode);
    @Transactional
    @Query("select enrolled from Classroom cl where cl.code =:class")
    int getEnrolledInClass(@Param("class") String classCode);

    @Modifying
    @Transactional
    @Query("update Classroom cl set cl.enrolled = cl.enrolled + 1 WHERE cl.code =:classCode")
    void enrollStudentInClass(@Param("class") String classCode);

    @Modifying
    @Transactional
    @Query("update Classroom cl set cl.enrolled = cl.enrolled - 1 WHERE cl.code =:classCode")
    void removeEnrollment(@Param("class") String classCode);
}
