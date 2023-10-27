package com.liraz.classmanagement.dtos.student;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Data Transfer Object that contains all necessary information on a registered student.")
public class StudentRequestDTO {

    @Schema(description = "Student registration number", example = "1056")
    private int registration;
    private String firstName;
    private String lastName;
    private String cpf;
    private String email;
    private boolean isActive;

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getRegistration() {
        return registration;
    }

    public void setRegistration(int registration) {
        this.registration = registration;
    }

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
