package com.liraz.classmanagement.domain.semester;
public enum SemesterStatus {
    ONGOING("ongoing"),
    CONCLUDED("concluded"),
    PLANNED("planned");

    private String status;

    SemesterStatus(String status){this.status = status;}
}
