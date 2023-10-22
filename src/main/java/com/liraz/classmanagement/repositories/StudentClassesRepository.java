package com.liraz.classmanagement.repositories;

import com.liraz.classmanagement.domain.student.StudentClass;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentClassesRepository extends JpaRepository<StudentClass,Long> {
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
}
