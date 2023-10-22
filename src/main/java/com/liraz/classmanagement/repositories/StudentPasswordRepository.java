package com.liraz.classmanagement.repositories;

import com.liraz.classmanagement.domain.student.StudentLogin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentPasswordRepository extends JpaRepository<StudentLogin, Long> {
    StudentLogin findByRegistration(int registration);
}
