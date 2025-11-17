package com.juneve.letterdiary.repository;

import com.juneve.letterdiary.entity.DiaryThread;
import com.juneve.letterdiary.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DiaryThreadRepository extends JpaRepository<DiaryThread, Long> {

    @Query("""
        SELECT DISTINCT t FROM DiaryThread t
        LEFT JOIN FETCH t.messages
        WHERE t.userA = :user OR t.userB = :user
    """)
    List<DiaryThread> findAllByUser(@Param("user") User user);
}
