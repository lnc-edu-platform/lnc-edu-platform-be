package com.education.education.user.controller;

import com.education.education.security.UserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/users")

public class UserController {

    @GetMapping("/me")
    public Map<String, Object> me(@AuthenticationPrincipal UserPrincipal principal){
        return Map.of(
                "userId",principal.getUserId(),
                "email",principal.getEmail()
        );
    }
}
