package com.liraz.classmanagement.services;

import com.liraz.classmanagement.domain.student.Student;
import com.liraz.classmanagement.domain.student.StudentClass;
import com.liraz.classmanagement.domain.student.StudentLogin;
import com.liraz.classmanagement.dtos.student.StudentClassDTO;
import com.liraz.classmanagement.dtos.student.StudentRequestDTO;
import com.liraz.classmanagement.repositories.StudentClassesRepository;
import com.liraz.classmanagement.repositories.StudentPasswordRepository;
import com.liraz.classmanagement.repositories.StudentRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudentPasswordRepository studentPasswordRepository;

    @Autowired
    private StudentClassesRepository studentClassesRepository;
    @Autowired
    private ClassroomService classroomService;

    //student related
    public Student createStudent(StudentRequestDTO studentRequestDTO){
        if(!registrationExists(studentRequestDTO.getRegistration())){

            Student student = new Student();

            student.setFirstName(studentRequestDTO.getFirstName());
            student.setLastName(studentRequestDTO.getLastName());
            student.setCpf(studentRequestDTO.getCpf());
            student.setRegistration(studentRequestDTO.getRegistration());
            student.setEmail(studentRequestDTO.getEmail());
            studentRepository.save(student);

            this.createLogin(studentRequestDTO);

            return student;
        }
        return null;
    }

    public Student deleteStudent(int registration){
        if(registrationExists(registration)){
            Student student = studentRepository.findByRegistration(registration);
            StudentLogin studentLogin = studentPasswordRepository.findByRegistration(registration);
            studentPasswordRepository.delete(studentLogin);
            studentRepository.delete(student);
            return student;
        }
        return null;
    }

    public Student update(StudentRequestDTO studentRequestDTO) {
        if(!registrationExists(studentRequestDTO.getRegistration())){

            Student student = studentRepository
                    .findByRegistration(studentRequestDTO.getRegistration());

            student.setFirstName(studentRequestDTO.getFirstName());
            student.setLastName(studentRequestDTO.getLastName());
            student.setEmail(studentRequestDTO.getEmail());
            studentRepository.save(student);

            this.updateLogin(studentRequestDTO);

            return student;
        }
        return null;
    }

    public Student findByRegistration(int registration) {
        return studentRepository.findByRegistration(registration);
    }
    private boolean registrationExists(int registration){
        return studentRepository.findByRegistration(registration) != null;
    }
    private void createLogin(StudentRequestDTO studentRequestDTO){
        StudentLogin studentLogin = new StudentLogin();
        studentLogin.setRegistration(studentRequestDTO.getRegistration());

        studentLogin.setPassword(hashPassword(studentRequestDTO.getPassword()));
        studentPasswordRepository.save(studentLogin);
    }

    private void updateLogin(StudentRequestDTO studentRequestDTO){
        StudentLogin studentLogin = studentPasswordRepository
                .findByRegistration(studentRequestDTO.getRegistration());

        studentLogin.setPassword(hashPassword(studentRequestDTO.getPassword()));
        studentPasswordRepository.save(studentLogin);
    }
    private String hashPassword(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }



    //not student related
    public StudentClass registerToClass(StudentClassDTO studentClassDTO) {
        if(registrationExists(studentClassDTO.getStudent())){
            StudentClass studentClass = new StudentClass();
            studentClass.setStudent(studentRepository
                    .findByRegistration(studentClassDTO.getStudent()));
            studentClass.setStudentStatus(studentClassDTO.getStudentStatus());

            studentClass.setClassroom(classroomService
                    .findByCode(studentClassDTO.getClassroom()));

            studentClassesRepository.save(studentClass);
            return studentClass;
        }

        return null;
    }

    public StudentClass deleteRegistrationToClass(int registration, String classCode) {
        if(registrationExists(registration)){
            Optional<StudentClass> deletedRegistration = studentClassesRepository
                    .getRegistrationToClass(classCode,
                            registration);


            if(deletedRegistration.isPresent()){
                studentClassesRepository
                        .deleteRegistrationToClass(classCode,
                                registration);
                return deletedRegistration.get();
            }
        }
        return null;
    }
}
