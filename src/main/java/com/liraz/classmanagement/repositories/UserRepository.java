package com.liraz.classmanagement.repositories;

import com.liraz.classmanagement.domain.user.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserModel, UUID> {
    UserModel findByLogin(String login);
}
