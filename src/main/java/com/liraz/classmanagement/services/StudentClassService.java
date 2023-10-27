package com.liraz.classmanagement.services;

import com.liraz.classmanagement.domain.classroom.Classroom;
import com.liraz.classmanagement.domain.classroom.ClassroomStatus;
import com.liraz.classmanagement.domain.student.Student;
import com.liraz.classmanagement.domain.student_classes.StudentClass;
import com.liraz.classmanagement.domain.student_classes.StudentStatus;
import com.liraz.classmanagement.dtos.student_class.ClassRecordDTO;
import com.liraz.classmanagement.dtos.student_class.StudentEnrollmentDetailDTO;
import com.liraz.classmanagement.exceptions.CustomizedException;
import com.liraz.classmanagement.exceptions.NotFoundException;
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


        if(this.checkIfStudentIsEnrolledInClass(classCode, studentRegistration))
            throw new CustomizedException(
                            "Student already enrolled in this class.");


        if(!this.checkIfClassIsUpForEnrollment(classCode))
            throw new CustomizedException(
                    "It is not possible to enroll to this classroom. It is not on the registration period");


        if(!this.studentIsActive(studentRegistration))
            throw new CustomizedException(
                    "Student registration deactivated.");


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

        if(!this.checkIfStudentIsEnrolledInClass(classCode, studentRegistration))
            throw new NotFoundException(
                            "Student isn't enrolled in this class.");


        if(this.checkIfClassIsUpForEnrollment(classCode)){ // so delete
            classroomService.removeEnrollment(classCode);
            Student student = studentService.findByRegistration(studentRegistration);
            CompletableFuture.runAsync(() -> {
                try {
                    emailSenderService.sendRemoveClassEnrollmentEmail(
                            student.getEmail(), student.getFirstName(),
                            classroomService.findByCode(classCode));
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
            });

        }else{ //change status to dropped
            StudentClass studentClass = this.
                    getEnrollment(classCode, studentRegistration);
            studentClass.setStudentStatus(StudentStatus.DROPPED);
        }
    }
    public List<StudentClass> fetchClassesForStudentInCurrentSemester(int registration){
        return repository.fetchClassesForStudentInCurrentSemester(registration);
    }

    public List<Student> getAllStudentsInAClass(String classCode) {
        List<Integer> studentsId = findStudentIdsByClassCode(classCode);
        List<Student> students = new ArrayList<>();
        for(Integer registration : studentsId){
            students.add(studentService.findByRegistration(registration));
        }
        return students;
    }

    public void setClassResults(ClassRecordDTO classRecordDTO) {

        if(classroomService.findByCode(classRecordDTO.getClassCode()) == null)
            throw new NotFoundException(
                    "Classroom not registered.");

        for(StudentEnrollmentDetailDTO studentEnrollmentDetailDTO : classRecordDTO.getStudentStatusUpdateList()){

            if(!this.checkIfStudentIsEnrolledInClass(classRecordDTO.getClassCode(),
                    studentEnrollmentDetailDTO.getStudentRegistration()))
                throw new NotFoundException(
                        "Student isn't enrolled in this class.");

            if(getEnrollment(classRecordDTO.getClassCode(),
                    studentEnrollmentDetailDTO.getStudentRegistration())
                    .getStudentStatus() == StudentStatus.DROPPED)
                throw new CustomizedException("You can not update the status " +
                        "of a student that dropped the class.");

            StudentClass sc = getEnrollment(classRecordDTO.getClassCode(),
                    studentEnrollmentDetailDTO.getStudentRegistration());
            sc.setStudentStatus(studentEnrollmentDetailDTO.getStudentStatus());

            repository.save(sc);
        }
    }

    public ClassRecordDTO getClassResults(String classCode) {
        ClassRecordDTO classRecordDTO = new ClassRecordDTO();
        classRecordDTO.setClassCode(classCode);
        List<Integer> students = repository.findStudentIdsByClassCode(classCode);
        List<StudentEnrollmentDetailDTO> studentEnrollmentDetailDTOS = new ArrayList<>();
        for(int registrationNumber : students){
            StudentEnrollmentDetailDTO studentStatus = new StudentEnrollmentDetailDTO();
            studentStatus.setStudentRegistration(registrationNumber);
            studentStatus.setStudentStatus(this.getEnrollment(classCode, registrationNumber)
                    .getStudentStatus());
            studentEnrollmentDetailDTOS.add(studentStatus);
        }
        classRecordDTO.setStudentStatusUpdateList(studentEnrollmentDetailDTOS);
        return classRecordDTO;
    }

    public boolean studentIsActive(int studentRegistration) {
        return studentService.studentIsActive(studentRegistration);
    }

    public List<StudentClass> fetchClassesForStudentInRegistrationPeriod(int registration) {
        return repository.fetchClassesForStudentInCurrentSemesterInRegistration(registration);
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void sendEndOfRegistrationEmail() throws MessagingException {

        List<Integer> studentsRegistrations = repository.getAllStudentsEnrolledInCurrentSemester();

        for(int reg : studentsRegistrations){

            Student student = studentService.findByRegistration(reg);
            List<StudentClass> studentsClassesCode = repository.
                    fetchClassesForStudentInCurrentSemester(reg);
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
