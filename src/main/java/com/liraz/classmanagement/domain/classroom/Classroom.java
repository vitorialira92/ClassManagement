package com.liraz.classmanagement.domain.classroom;

import com.liraz.classmanagement.domain.semester.Semester;
import jakarta.persistence.*;

@Entity
@Table(name = "classrooms")
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "classroom_code", unique = true, nullable = false)
    private String code;
    @Column(name = "classroom_name", nullable = false)
    private String name;
    @Column(nullable = false)
    private String professor;
    @Column(nullable = false)
    private int hoursWeek;
    @Column(name = "classroom_status", nullable = false)
    private ClassroomStatus status;
    @Column(nullable = false)
    private int seats;
    @Column(nullable = false)
    private int enrolled;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public int getHoursWeek() {
        return hoursWeek;
    }

    public void setHoursWeek(int hoursWeek) {
        this.hoursWeek = hoursWeek;
    }

    public ClassroomStatus getStatus() {
        return status;
    }

    public void setStatus(ClassroomStatus status) {
        this.status = status;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public int getEnrolled() {
        return enrolled;
    }

    public void setEnrolled(int enrolled) {
        this.enrolled = enrolled;
    }
}
