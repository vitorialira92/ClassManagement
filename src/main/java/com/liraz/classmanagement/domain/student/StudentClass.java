package com.liraz.classmanagement.domain.student;

import com.liraz.classmanagement.domain.semester.Semester;
import com.liraz.classmanagement.domain.classroom.Classroom;
import jakarta.persistence.*;

@Entity
@Table(name = "student_classes")
public class StudentClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "classroom_code", referencedColumnName = "classroom_code")
    private Classroom classroom;
    @ManyToOne
    @JoinColumn(name = "student", referencedColumnName = "registration")
    private Student student;

    @Column(name = "student_status", nullable = false)
    private StudentStatus studentStatus;
    @ManyToOne
    @JoinColumn(name = "semester_code", referencedColumnName = "semester_code")
    private Semester semester;

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public StudentStatus getStudentStatus() {
        return studentStatus;
    }

    public void setStudentStatus(StudentStatus studentStatus) {
        this.studentStatus = studentStatus;
    }
}
