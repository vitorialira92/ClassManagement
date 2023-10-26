package com.liraz.classmanagement.services;

import com.liraz.classmanagement.domain.student.Student;
import com.liraz.classmanagement.dtos.student.StudentRegisterDTO;
import com.liraz.classmanagement.dtos.student.StudentRequestDTO;
import com.liraz.classmanagement.exceptions.CustomizedException;
import com.liraz.classmanagement.exceptions.NotFoundException;
import com.liraz.classmanagement.repositories.StudentRepository;
import com.liraz.classmanagement.services.email.EmailSenderService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private EmailSenderService emailSenderService;

    public Student createStudent(StudentRegisterDTO studentRegisterDTO){
        if(!isCpfValid(studentRegisterDTO.getCpf()))
            throw new CustomizedException(
                            "Invalid CPF.");


        if(this.findByCpf(studentRegisterDTO.getCpf()) != null)
            throw new CustomizedException(
                            "Student already registered.");


        Student student = new Student();
        student.setFirstName(studentRegisterDTO.getFirstName());
        student.setLastName(studentRegisterDTO.getLastName());
        student.setCpf(studentRegisterDTO.getCpf());
        student.setEmail(studentRegisterDTO.getEmail());
        student.setActive(true);
        student = studentRepository.save(student);

        Student finalStudent = student;
        CompletableFuture.runAsync(() -> {
            try {
                emailSenderService.sendRegistrationEmail(studentRegisterDTO.getEmail(),
                        studentRegisterDTO.getFirstName(), finalStudent.getRegistration());
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        });

        return student;
    }


    public Student update(StudentRequestDTO studentRequestDTO) {

        if(this.findByCpf(studentRequestDTO.getCpf()) == null)
            throw new NotFoundException(
                            "Student not found.");
        Student student = studentRepository
                .findByRegistration(studentRequestDTO.getRegistration());
        student.setFirstName(studentRequestDTO.getFirstName());
        student.setLastName(studentRequestDTO.getLastName());
        student.setEmail(studentRequestDTO.getEmail());
        student.setActive(studentRequestDTO.isActive());
        studentRepository.save(student);

        return student;
    }

    public Student findByCpf(String cpf){
        return studentRepository.findByCpf(cpf);
    }

    public Student findByRegistration(int registration) {
        Student student = studentRepository.findByRegistration(registration);
        
        if(student == null)
            throw new NotFoundException("Student not found.");

        return student;
    }

    public void deactivateStudent(int registration){
        Student student = studentRepository.findByRegistration(registration);

        if(student != null){
            student.setActive(false);
            studentRepository.save(student);
            return;
        }
        throw new NotFoundException("Student not found.");
    }
    private boolean registrationExists(int registration){
        return studentRepository.findByRegistration(registration) != null;
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public boolean studentIsActive(int registration) {
        return studentRepository.findByRegistration(registration).isActive();
    }

    private boolean isCpfValid(String cpf){
        if(cpf.length() != 11){ return false; }
        System.out.println("Passou na ver de tamanho");
        if(checkIfCpfIsARepeatedSequence(cpf)){ return false; }
        System.out.println("Passou na ver de sequencia");
        int sum = 0;

        for(int i = 0; i < 9; i++){
            sum += (10 - i) * Integer.parseInt(String.valueOf(cpf.charAt(i)));
        }

        int rest = sum % 11;
        if(rest < 2 && cpf.charAt(9) != '0'){ return false; }
        else if(rest >= 2 &&
                Integer.parseInt(String.valueOf(cpf.charAt(9))) != (11 - rest)){ return false; }
        System.out.println("Passou na ver de primeiro dig");
        sum = 0;

        for(int i = 0; i < 9; i++){
            sum += (11 - i) * Integer.parseInt(String.valueOf(cpf.charAt(i)));
        }

        sum +=  (11 - rest) * 2;

        rest = sum % 11;

        if(rest < 2 && cpf.charAt(10) != '0'){ return false; }
        else if(rest >= 2 &&
                Integer.parseInt(String.valueOf(cpf.charAt(10))) != (11 - rest)){ return false; }
        System.out.println("Passou na ver de seg dig");
        return true;
    }

    private boolean checkIfCpfIsARepeatedSequence(String cpf){
        ArrayList<String> invalidSequences = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            String number = i + "";
            invalidSequences.add(number.repeat(11));
        }
        return invalidSequences.contains(cpf);

    }

}
