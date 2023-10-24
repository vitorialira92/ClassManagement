package com.liraz.classmanagement.domain.semester;

import com.liraz.classmanagement.domain.semester.SemesterStatus;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "semesters")
public class Semester {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, name = "semester_code")
    private String semesterCode;
    @Column(nullable = false)
    private LocalDate registrationStart;
    @Column(nullable = false)
    private LocalDate registrationEnd;
    @Column(nullable = false)
    private LocalDate semesterStart;
    @Column(nullable = false)
    private LocalDate semesterEnd;
    @Column(nullable = false)
    private SemesterStatus semesterStatus;

    public SemesterStatus getSemesterStatus() {
        return semesterStatus;
    }

    public void setSemesterStatus(SemesterStatus semesterStatus) {
        this.semesterStatus = semesterStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSemesterCode() {
        return semesterCode;
    }

    public void setSemesterCode(String semesterCode) {
        this.semesterCode = semesterCode;
    }

    public LocalDate getRegistrationStart() {
        return registrationStart;
    }

    public void setRegistrationStart(LocalDate registrationStart) {
        this.registrationStart = registrationStart;
    }

    public LocalDate getRegistrationEnd() {
        return registrationEnd;
    }

    public void setRegistrationEnd(LocalDate registrationEnd) {
        this.registrationEnd = registrationEnd;
    }

    public LocalDate getSemesterStart() {
        return semesterStart;
    }

    public void setSemesterStart(LocalDate semesterStart) {
        this.semesterStart = semesterStart;
    }

    public LocalDate getSemesterEnd() {
        return semesterEnd;
    }

    public void setSemesterEnd(LocalDate semesterEnd) {
        this.semesterEnd = semesterEnd;
    }
}
