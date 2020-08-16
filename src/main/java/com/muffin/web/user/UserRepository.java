package com.muffin.web.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, IUserRepository {

    User save(User user);

    Optional<User> findByEmailId(String emailId);

    boolean existsByEmailId(String emailId);
}
