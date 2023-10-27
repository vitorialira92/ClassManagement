package com.liraz.classmanagement.dtos.student_class;

import com.liraz.classmanagement.domain.student_classes.StudentStatus;

public class StudentStatusUpdateDTO {
    private int studentRegistration;
    private StudentStatus studentStatus;

    public int getStudentRegistration() {
        return studentRegistration;
    }

    public void setStudentRegistration(int studentRegistration) {
        this.studentRegistration = studentRegistration;
    }

    public StudentStatus getStudentStatus() {
        return studentStatus;
    }

    public void setStudentStatus(StudentStatus studentStatus) {
        this.studentStatus = studentStatus;
    }
}
