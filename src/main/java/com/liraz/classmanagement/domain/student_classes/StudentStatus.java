package com.liraz.classmanagement.domain.student_classes;

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
