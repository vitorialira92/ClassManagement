package com.liraz.classmanagement.services.emails;

import com.liraz.classmanagement.domain.classroom.Classroom;
import com.liraz.classmanagement.domain.student.Student;
import com.liraz.classmanagement.domain.student_classes.StudentClass;
import com.liraz.classmanagement.repositories.SemesterRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private SemesterRepository semesterRepository;

    private void sendHtmlMail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true); // Set to true to indicate the text is HTML

        emailSender.send(message);
    }

    public void sendRegistrationEmail(String email, String name) throws MessagingException {
        //email falando p criar login
        String title = "Welcome to our system!";
        String message = "<b>Hello, " + name + "! It is great to see you here. </b>";
        sendHtmlMail(email, title, message);
    }

    public void sendClassEnrollmentEmail(String email, String name, Classroom classroom) throws MessagingException {
        String title = "Welcome to our system!";
        String message = "<b>Hello, " + name + "! It is great to see you here. </b>";
        sendHtmlMail(email, title, message);
    }

    public void sendRemoveClassEnrollmentEmail(String email, String name, Classroom classroom) throws MessagingException{
        String title = "Welcome to our system!";
        String message = "<b>Hello, " + name + "! It is great to see you here. </b>";
        sendHtmlMail(email, title, message);
    }

    public void sendStudentsCurrentClassesEmail(Student student, List<Classroom> classes){

    }
/*
    @Scheduled(cron = "0 0 12 * * ?")
    public void checkEnrollmentPeriod() {
        List<Semester> semesters = repository.findCurrentSemesters();
        for (Semester semester : semesters) {
            //the day after
            if (LocalDate.now().equals(semester.getRegistrationEnd().plusDays(1))) {
                List<Integer> studentsRegistration = studentService.findAllStudentsInASemester(semester.getSemesterCode());
                for (Integer student : studentsRegistration) {
                    emailSenderService.sendRegistrationPeriodEnd(studentService.findByRegistration(student),
                            studentService.getAllEnrolledClasses(student));
                }
                repository.save(semester);
            }
        }
    }*/

}
