package com.liraz.classmanagement.dtos.student_class;

import java.util.List;

public class ClassRecordDTO {
    private String classCode;
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
