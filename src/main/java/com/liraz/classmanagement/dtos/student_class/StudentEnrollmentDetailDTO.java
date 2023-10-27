package com.liraz.classmanagement.dtos.student_class;

import com.liraz.classmanagement.domain.student_classes.StudentStatus;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Contains student registration number and student status in a class.")
public class StudentEnrollmentDetailDTO {
    @Schema(description = "Student registration number", example = "1058")
    private int studentRegistration;
    @Schema(description = "Student's status", example = "FAILED")
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
