package com.metacoding.springv2.admin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    // cos만 접속가능. (인증, 권한 필요함 admin)
    @GetMapping("/admin/test")
    public String test() {
        return "<h1>관리자페이지</h1>";
    }
}
