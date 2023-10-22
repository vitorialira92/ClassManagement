package com.liraz.classmanagement.domain.student;

import jakarta.persistence.*;

@Entity
@Table(name = "student_password")
public class StudentLogin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "student_registration")
    private int registration;
    @Column(name = "student_password")
    private String password;

    public int getRegistration() {
        return registration;
    }

    public void setRegistration(int registration) {
        this.registration = registration;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
