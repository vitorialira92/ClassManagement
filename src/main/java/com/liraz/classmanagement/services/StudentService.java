package com.liraz.classmanagement.services;

import com.liraz.classmanagement.domain.student.Student;
import com.liraz.classmanagement.dtos.student.StudentRegisterDTO;
import com.liraz.classmanagement.dtos.student.StudentRequestDTO;
import com.liraz.classmanagement.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public Student createStudent(StudentRegisterDTO studentRegisterDTO){

        if(findByCpf(studentRegisterDTO.getCpf()) != null)
            return null;

        Student student = new Student();
        student.setFirstName(studentRegisterDTO.getFirstName());
        student.setLastName(studentRegisterDTO.getLastName());
        student.setCpf(studentRegisterDTO.getCpf());
        student.setEmail(studentRegisterDTO.getEmail());
        student.setActive(true);
        student = studentRepository.save(student);

        return student;
    }


    public Student update(StudentRequestDTO studentRequestDTO) {

        if(registrationExists(studentRequestDTO.getRegistration())){

            Student student = studentRepository
                    .findByRegistration(studentRequestDTO.getRegistration());

            student.setFirstName(studentRequestDTO.getFirstName());
            student.setLastName(studentRequestDTO.getLastName());
            student.setEmail(studentRequestDTO.getEmail());
            student.setActive(studentRequestDTO.isActive());
            studentRepository.save(student);

            return student;
        }
        return null;
    }

    public Student findByCpf(String cpf){
        return studentRepository.findByCpf(cpf);
    }

    public Student findByRegistration(int registration) {
        return studentRepository.findByRegistration(registration);
    }

    public boolean deactivateStudent(int registration){
        Student student = findByRegistration(registration);

        if(student != null){
            student.setActive(false);
            studentRepository.save(student);
            return true;
        }
        return false;

    }
    private boolean registrationExists(int registration){
        return studentRepository.findByRegistration(registration) != null;
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }
}
