package com.juneve.letterdiary.repository;

import com.juneve.letterdiary.entity.DiaryThread;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryThreadRepository extends JpaRepository<DiaryThread, Long> {
}
