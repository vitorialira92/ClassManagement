package com.liraz.classmanagement.dtos.student_class;

import java.util.List;

public class ClassEndDTO {
    private String classCode;
    private List<StudentStatusUpdateDTO> studentStatusUpdateList;

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public List<StudentStatusUpdateDTO> getStudentStatusUpdateList() {
        return studentStatusUpdateList;
    }

    public void setStudentStatusUpdateList(List<StudentStatusUpdateDTO> studentStatusUpdateList) {
        this.studentStatusUpdateList = studentStatusUpdateList;
    }
}
