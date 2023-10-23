package com.liraz.classmanagement.domain.semester;
public enum SemesterStatus {
    ONGOIN("ongoing"),
    CONCLUDED("concluded"),
    PLANNED("planned");

    private String status;

    SemesterStatus(String status){this.status = status;}
}
