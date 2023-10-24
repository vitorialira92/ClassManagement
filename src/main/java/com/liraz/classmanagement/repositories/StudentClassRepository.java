package com.liraz.classmanagement.repositories;

import com.liraz.classmanagement.domain.student_classes.StudentClass;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentClassRepository extends JpaRepository<StudentClass,Long> {
    @Modifying
    @Transactional
    @Query(
            "DELETE FROM StudentClass sc WHERE sc.classroom.code = :classroom" +
                    " AND sc.student.registration = :student")
    void deleteRegistrationToClass(@Param("classroom")String classroom, @Param("student")int registration);

    @Transactional
    @Query(
            "select sc from StudentClass sc WHERE sc.classroom.code = :classroom" +
                    " AND sc.student.registration = :student")
    Optional<StudentClass> getRegistrationToClass(@Param("classroom")String classroom, @Param("student")int registration);

    @Transactional
    @Query("select sc from StudentClass sc where sc.classroom.status = ONGOING and sc.student.registration =:student")
    List<StudentClass> fetchClassesForStudentInCurrentSemester(@Param("student") int registration);

    @Transactional
    @Query("select distinct sc.student.registration from StudentClass sc where sc.semester.semesterCode =:semester")
    List<Integer> getAllStudentsInASemester(@Param("semester") String semesterCode);

    @Transactional
    @Query("select distinct sc from StudentClass sc where sc.classroom.code =:classCode " +
            "AND sc.student.registration =:studentRegistration")
    StudentClass getEnrollment(@Param("classCode") String classCode,
                               @Param("studentRegistration") int registration);

    @Transactional
    @Query("select distinct sc.student.registration from StudentClass sc where sc.classroom.code =:classCode")
    List<Integer> findStudentIdsByClassCode(@Param("classCode")String classCode);

    @Transactional
    @Query("select distinct sc.student.registration from StudentClass sc where sc.classroom.status =:ONGOING")
    List<Integer>getAllStudentsEnrolledInCurrentSemester();
}
