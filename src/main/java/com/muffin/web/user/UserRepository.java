package com.muffin.web.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, IUserRepository {

    Optional<User> findByEmailId(String emailId);

    Boolean existsByEmailId(String emailId);

    Optional<User> findByEmailId(String emailId);

    boolean existsByEmailId(String emailId);
}
