package com.liraz.classmanagement.services;

import com.liraz.classmanagement.domain.classroom.Classroom;
import com.liraz.classmanagement.domain.classroom.ClassroomStatus;
import com.liraz.classmanagement.domain.student.Student;
import com.liraz.classmanagement.domain.student_classes.StudentClass;
import com.liraz.classmanagement.domain.student_classes.StudentStatus;
import com.liraz.classmanagement.repositories.StudentClassRepository;
import com.liraz.classmanagement.services.email.EmailSenderService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class StudentClassService {

    @Autowired
    private StudentClassRepository repository;
    @Autowired
    private ClassroomService classroomService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private SemesterService semesterService;
    @Autowired
    private EmailSenderService emailSenderService;

    public StudentClass getEnrollment(String classCode, int registration){
        return repository.getEnrollment(classCode, registration);
    }
    public boolean checkIfStudentIsEnrolledInClass(String classCode, int studentRegistration){
        return (repository.getEnrollment(classCode, studentRegistration) != null);
    }

    public boolean checkIfClassIsUpForEnrollment(String classCode){
        return classroomService.getClassStatus(classCode) == ClassroomStatus.REGISTRATION;
    }

    public StudentClass enrollStudentInClass(String classCode, int studentRegistration, String semester) throws MessagingException {

        if(!checkIfClassIsUpForEnrollment(classCode) || checkIfStudentIsEnrolledInClass(classCode, studentRegistration))
            return null;

        StudentClass studentClass = new StudentClass();

        studentClass.setStudent(studentService.findByRegistration(studentRegistration));
        studentClass.setStudentStatus(StudentStatus.ONGOING);
        studentClass.setClassroom(classroomService.findByCode(classCode));
        studentClass.setSemester(semesterService.findByCode(semester));

        classroomService.enrollStudentInClass(classCode);

        StudentClass sc = repository.save(studentClass);

        CompletableFuture.runAsync(() -> {
            try {
                emailSenderService.sendClassEnrollmentEmail(
                        studentService.findByRegistration(studentRegistration).getEmail(),
                            studentService.findByRegistration(studentRegistration).getFirstName(),
                                classroomService.findByCode(classCode));
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        });
        return sc;

    }

    public void removeEnrollment(String classCode, int studentRegistration) throws MessagingException {
        classroomService.removeEnrollment(classCode);
        Student student = studentService.findByRegistration(studentRegistration);
        emailSenderService.sendRemoveClassEnrollmentEmail(
                student.getEmail(), student.getFirstName(),
                    classroomService.findByCode(classCode));

    }
    public List<StudentClass> fetchClassesForStudentInCurrentSemester(int registration){
        List<StudentClass> classesRegistration = repository.fetchClassesForStudentInCurrentSemester(registration);
        classesRegistration.addAll(repository.fetchClassesForStudentInCurrentSemesterInRegistration(registration));
        return classesRegistration;
    }

    public List<Student> getAllStudentsInAClass(String classCode) {
        List<Integer> studentsId = findStudentIdsByClassCode(classCode);
        List<Student> students = new ArrayList<>();
        for(Integer registration : studentsId){
            students.add(studentService.findByRegistration(registration));
        }
        return students;
    }

    @Scheduled(cron = "0 37 13 * * ?")
    public void sendEndOfRegistrationEmail() throws MessagingException {
        System.out.println("entrou no scheduled");
        List<Integer> studentsRegistrations = repository.getAllStudentsEnrolledInCurrentSemester();

        for(int reg : studentsRegistrations){
            System.out.println("reg:" + reg);
           Student student = studentService.findByRegistration(reg);
            List<StudentClass> studentsClassesCode = repository.
                    fetchClassesForStudentInCurrentSemesterInRegistration(reg);
            List<Classroom> classes = new ArrayList<>();
            for(StudentClass stc : studentsClassesCode){
                classes.add(stc.getClassroom());
            }
            emailSenderService.sendStudentsCurrentClassesEmail(student, classes);
        }

    }

    private List<Integer> findStudentIdsByClassCode(String classCode){
        return repository.findStudentIdsByClassCode(classCode);
    }
}