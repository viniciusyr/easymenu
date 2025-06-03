package com.easymenu.user;

import com.easymenu.user.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserModel, UUID> {
    Optional<UserModel> findByName(String name);
    boolean existsByEmail(String email);
    boolean existsByName(String username);
    List<UserModel> findAllByStatus(UserStatus status);
}
