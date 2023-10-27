package com.liraz.classmanagement.domain.student_classes;

import com.fasterxml.jackson.databind.annotation.EnumNaming;
import io.swagger.v3.oas.annotations.media.Schema;
public enum StudentStatus {
    @Schema(description = "Student is approved.")
    PASSED("passed"),
    @Schema(description = "Student dropped the class.")
    DROPPED("dropped"),
    @Schema(description = "Student failed the class.")
    FAILED("failed"),
    @Schema(description = "Student is currently studying.")
    ONGOING("ongoing");

    private String studentStatus;

    StudentStatus(String studentStatus){
        this.studentStatus = studentStatus;
    }
}
