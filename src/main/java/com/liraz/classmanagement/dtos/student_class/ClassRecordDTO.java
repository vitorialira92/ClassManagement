package com.liraz.classmanagement.dtos.student_class;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Data Transfer Object that contains a class code and the status of all the students enrolled in this class.")
public class ClassRecordDTO {
    @Schema(description = "Unique identifier of the class", example = "1568A")
    private String classCode;
    @Schema(description = "List of the status and code of all students in this class")
    private List<StudentEnrollmentDetailDTO> studentStatusUpdateList;

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public List<StudentEnrollmentDetailDTO> getStudentStatusUpdateList() {
        return studentStatusUpdateList;
    }

    public void setStudentStatusUpdateList(List<StudentEnrollmentDetailDTO> studentStatusUpdateList) {
        this.studentStatusUpdateList = studentStatusUpdateList;
    }
}
