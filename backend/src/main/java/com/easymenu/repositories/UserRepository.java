package com.easymenu.repositories;

import com.easymenu.enums.UserStatus;
import com.easymenu.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserModel, UUID> {
    boolean existsByEmail(String email);
    boolean existsByName(String username);
    List<UserModel> findAllByStatus(UserStatus status);
}
