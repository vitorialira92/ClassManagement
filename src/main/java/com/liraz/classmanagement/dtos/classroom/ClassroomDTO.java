package com.liraz.classmanagement.dtos.classroom;

import com.liraz.classmanagement.domain.classroom.ClassroomStatus;

public class ClassroomDTO {
    private String code;
    private String name;
    private String professor;
    private int hoursWeek;
    private int seats;
    private int enrolled;
    private String semesterCode;

    public String getSemesterCode() {
        return semesterCode;
    }

    public void setSemesterCode(String semesterCode) {
        this.semesterCode = semesterCode;
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
