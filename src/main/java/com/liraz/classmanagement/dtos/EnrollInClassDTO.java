package com.liraz.classmanagement.dtos;

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
