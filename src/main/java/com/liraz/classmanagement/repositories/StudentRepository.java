package com.liraz.classmanagement.repositories;

import com.liraz.classmanagement.domain.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByRegistration(int registration);

}
