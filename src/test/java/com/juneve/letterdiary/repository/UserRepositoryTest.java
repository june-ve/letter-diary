package com.juneve.letterdiary.repository;

import com.juneve.letterdiary.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("User 저장 및 조회 테스트 (이메일 기준)")
    void saveAndFindUserByEmail() {
        // given
        User user = User.builder()
                .email("aaa123@email.com")
                .password("12341234")
                .nickname("aaa")
                .build();

        // when
        User savedUser = userRepository.save(user);

        // then
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo("aaa123@email.com");
        System.out.println("✅ 저장된 사용자 ID: " + savedUser.getId());
    }
}
