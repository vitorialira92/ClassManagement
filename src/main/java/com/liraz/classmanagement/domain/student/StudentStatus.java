package com.liraz.classmanagement.domain.student;

public enum StudentStatus {
    PASSED("passed"),
    DROPPED("dropped"),
    FAILED("failed"),
    ONGOING("ongoing");

    private String studentStatus;

    StudentStatus(String studentStatus){
        this.studentStatus = studentStatus;
    }
}
