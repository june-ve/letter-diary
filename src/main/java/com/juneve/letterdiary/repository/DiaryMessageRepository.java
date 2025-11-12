package com.juneve.letterdiary.repository;

import com.juneve.letterdiary.entity.DiaryMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryMessageRepository extends JpaRepository<DiaryMessage, Long> {
}
