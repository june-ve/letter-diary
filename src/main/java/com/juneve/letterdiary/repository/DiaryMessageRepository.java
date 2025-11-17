package com.juneve.letterdiary.repository;

import com.juneve.letterdiary.entity.DiaryMessage;
import com.juneve.letterdiary.entity.DiaryThread;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryMessageRepository extends JpaRepository<DiaryMessage, Long> {

    Page<DiaryMessage> findByThreadOrderByCreatedAtDesc(DiaryThread thread, Pageable pageable);
}
