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

    default DiaryThread findThreadById(Long threadId) {
        return findById(threadId)
                .orElseThrow(() -> new IllegalArgumentException("해당 일기장이 존재하지 않습니다."));
    }

    /**
     * (UserA, UserB) 조합 혹은 (UserB, UserA) 조합이 존재하는지 확인
     */
    boolean existsByUserAAndUserBOrUserAAndUserB(User userA1, User userB1, User userA2, User userB2);
}
