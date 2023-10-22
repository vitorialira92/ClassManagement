package com.liraz.classmanagement.dtos.student;

import com.liraz.classmanagement.domain.student.StudentStatus;

public class StudentClassDTO {
    private String classroom;

    private int student;

    private StudentStatus studentStatus;

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public int getStudent() {
        return student;
    }

    public void setStudent(int student) {
        this.student = student;
    }

    public StudentStatus getStudentStatus() {
        return studentStatus;
    }

    public void setStudentStatus(StudentStatus studentStatus) {
        this.studentStatus = studentStatus;
    }
}