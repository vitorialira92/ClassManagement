package com.liraz.classmanagement.repositories;

import com.liraz.classmanagement.domain.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByRegistration(int registration);

    Student findByCpf(String cpf);

    List<Student> findAll();

}
