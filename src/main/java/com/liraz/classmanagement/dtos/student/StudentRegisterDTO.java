package com.liraz.classmanagement.dtos.student;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Data Transfer Object that contains all necessary information on student to register them.")
public class StudentRegisterDTO {

    private String firstName;
    private String lastName;
    @Schema(description = "Student's CPF. Must be valid.", example = "32792329025")
    private String cpf;

    private String email;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
