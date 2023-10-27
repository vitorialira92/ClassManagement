package com.liraz.classmanagement.dtos.student_class;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Data Transfer Object that contains the class code, student registration number and semester code for a enrollment.")
public class EnrollInClassDTO {
    private String classCode;
    private int studentRegistration;
    private String semester;

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public int getStudentRegistration() {
        return studentRegistration;
    }

    public void setStudentRegistration(int studentRegistration) {
        this.studentRegistration = studentRegistration;
    }
}
