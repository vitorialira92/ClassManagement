package com.liraz.classmanagement.services.email;

import com.liraz.classmanagement.domain.classroom.Classroom;
import com.liraz.classmanagement.domain.student.Student;
import com.liraz.classmanagement.repositories.SemesterRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
        String title = "Welcome to our college!";
        String beginning = "<!DOCTYPE html> <html lang=\"pt-br\"> <head> <meta charset=\"UTF-8\"> " +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"><title>E-mail Template</title> " +
                "<style> body { font-family: Arial, sans-serif; padding: 20px; margin: 0; " +
                "background-color: #f4f4f4;} .email-container { max-width: 600px; margin: 0 auto; " +
                "background-color: #ffffff; padding: 20px;border-radius: 4px;" +
                "box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);}.header, " +
                ".footer {background-color: #333333;color: #ffffff;padding: 10px 0;text-align: " +
                "center;border-radius: 4px;}.content {margin-top: 20px;margin-bottom: 20px;}</style>" +
                "</head><body><div class=\"email-container\"><div class=\"header\"><h2>COLLEGE SYSTEM</h2>" +
                "</div><div class=\"content\"><p>HELLO, " + name.toUpperCase() +",</p>";
        String classText = "<p>You have been registered to our system! Now you can go and " +
                "create a login using your CPF information.<br><br></p>";
        String ending = "</div><div class=\"footer\">" +
                "All rights reserved - COLLEGE SYSTEM, 2023</div></div></body></html>";
        String message = beginning + classText + ending;
        sendHtmlMail(email, title, message);
    }

    public void sendClassEnrollmentEmail(String email, String name, Classroom classroom) throws MessagingException {
        String title = "Enrollment to class confirmed!.";
        String beginning = "<!DOCTYPE html> <html lang=\"pt-br\"> <head> <meta charset=\"UTF-8\"> " +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"><title>E-mail Template</title> " +
                "<style> body { font-family: Arial, sans-serif; padding: 20px; margin: 0; " +
                "background-color: #f4f4f4;} .email-container { max-width: 600px; margin: 0 auto; " +
                "background-color: #ffffff; padding: 20px;border-radius: 4px;" +
                "box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);}.header, " +
                ".footer {background-color: #333333;color: #ffffff;padding: 10px 0;text-align: " +
                "center;border-radius: 4px;}.content {margin-top: 20px;margin-bottom: 20px;}</style>" +
                "</head><body><div class=\"email-container\"><div class=\"header\"><h2>COLLEGE SYSTEM</h2>" +
                "</div><div class=\"content\"><p>HELLO, " + name.toUpperCase() +",</p>";
        String classText = "<p>You just enrolled to a class. " +
                "CLASS CODE:" + classroom.getCode() + "<br>" +
                "CLASS NAME:" + classroom.getName() + "<br>" +
                "PROFESSOR: " + classroom.getProfessor() + "<br></p>";
        String ending = "</div><div class=\"footer\">" +
                "All rights reserved - COLLEGE SYSTEM, 2023</div></div></body></html>";
        String message = beginning + classText + ending;
        sendHtmlMail(email, title, message);
    }

    public void sendRemoveClassEnrollmentEmail(String email, String name, Classroom classroom) throws MessagingException{
        String title = "Class enrollment canceled.";
        String beginning = "<!DOCTYPE html> <html lang=\"pt-br\"> <head> <meta charset=\"UTF-8\"> " +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"><title>E-mail Template</title> " +
                "<style> body { font-family: Arial, sans-serif; padding: 20px; margin: 0; " +
                "background-color: #f4f4f4;} .email-container { max-width: 600px; margin: 0 auto; " +
                "background-color: #ffffff; padding: 20px;border-radius: 4px;" +
                "box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);}.header, " +
                ".footer {background-color: #333333;color: #ffffff;padding: 10px 0;text-align: " +
                "center;border-radius: 4px;}.content {margin-top: 20px;margin-bottom: 20px;}</style>" +
                "</head><body><div class=\"email-container\"><div class=\"header\"><h2>COLLEGE SYSTEM</h2>" +
                "</div><div class=\"content\"><p>HELLO, " + name.toUpperCase() +",</p>";
        String classText = "<p>You just cancel your enrollment to a class. " +
                "CLASS CODE:" + classroom.getCode() + "<br>" +
                "CLASS NAME:" + classroom.getName() + "<br>" +
                "PROFESSOR: " + classroom.getProfessor() + "<br></p>";
        String ending = "</div><div class=\"footer\">" +
                "All rights reserved - COLLEGE SYSTEM, 2023</div></div></body></html>";
        String message = beginning + classText + ending;
                sendHtmlMail(email, title, message);
    }

    public void sendStudentsCurrentClassesEmail(Student student, List<Classroom> classes) throws MessagingException {
        String title = "Classes you enrolled this semester";
        String beginning = "<!DOCTYPE html> <html lang=\"pt-br\"> <head> <meta charset=\"UTF-8\"> " +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"><title>E-mail Template</title> " +
                "<style> body { font-family: Arial, sans-serif; padding: 20px; margin: 0; " +
                "background-color: #f4f4f4;} .email-container { max-width: 600px; margin: 0 auto; " +
                "background-color: #ffffff; padding: 20px;border-radius: 4px;" +
                "box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);}.header, " +
                ".footer {background-color: #333333;color: #ffffff;padding: 10px 0;text-align: " +
                "center;border-radius: 4px;}.content {margin-top: 20px;margin-bottom: 20px;}</style>" +
                "</head><body><div class=\"email-container\"><div class=\"header\"><h2>COLLEGE SYSTEM</h2>" +
                "</div><div class=\"content\"><p>HELLO, " + student.getFirstName().toUpperCase() +",</p>";
        String classText = "<p>The registration to classes period is over. Here are all the classes " +
                "you are enrolled to: <br>";
        String allClasses = "";
        for (Classroom classroom : classes){
            allClasses += "CLASS CODE:" + classroom.getCode() + "<br>" +
                    "CLASS NAME:" + classroom.getName() + "<br>" +
                    "PROFESSOR: " + classroom.getProfessor() + "<br> + " +
                    "----------------------------------------<br>" + "</p>";
        }

        String ending = "Have a good semester.</div><div class=\"footer\">" +
                "All rights reserved - COLLEGE SYSTEM, 2023</div></div></body></html>";
        String message = beginning + classText + ending;
        sendHtmlMail(student.getEmail(), title, message);
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
