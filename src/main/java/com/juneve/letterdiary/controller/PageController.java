package com.juneve.letterdiary.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    /** 홈페이지 접속 시 로그인 페이지로 이동 */
    @GetMapping("/")
    public String home() {
        return "user/login";
    }

    /** 로그인 페이지 */
    @GetMapping("/login")
    public String loginPage() {
        return "user/login";
    }

    /** 책상(목록) 페이지 */
    @GetMapping("/diary")
    public String diaryDeskPage() {
        return "diary/desk";
    }

    /** 일기장 페이지 */
    @GetMapping("/diary/{threadId}")
    public String diaryDetailPage() {
        return "diary/thread";
    }
}
