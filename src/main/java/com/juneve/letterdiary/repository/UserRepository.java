package com.juneve.letterdiary.repository;

import com.juneve.letterdiary.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    default User findUserByEmail(String email) {
        return findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("사용자가 존재하지 않습니다."));
    }
}
