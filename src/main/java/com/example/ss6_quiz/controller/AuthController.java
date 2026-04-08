package com.example.ss6_quiz.controller;

import com.example.ss6_quiz.config.JwtAuthenticationFilter;
import com.example.ss6_quiz.entity.Users;
import com.example.ss6_quiz.repository.IUsersRepository;
import com.example.ss6_quiz.service.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private IUsersRepository usersRepository;
    @Autowired
    private IUsersService usersService;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/me")
    public ResponseEntity<?> getMe() {
        var auth = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return ResponseEntity.status(401).body("Chưa đăng nhập");
        }

        String username = auth.getName();

        return usersRepository.findByUsername(username)
                .map(user -> ResponseEntity.ok(Map.of(
                        "id", user.getId(),
                        "username", user.getUsername(),
                        "email", user.getEmail(),
                        "xp", user.getXp(),
                        "point", user.getPoint()
                )))
                .orElse(ResponseEntity.status(404).build());
    }


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Users user) {
        try {
            usersService.registerUser(user);
            return ResponseEntity.ok("Đăng ký thành công!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");
        try {
            Users user = usersService.login(username, password);
            String token = jwtAuthenticationFilter.generateToken(user.getUsername(), user.getRoles().getName());
            String redisKey = "auth:user:" + user.getId();
            // Lưu Token này với thời gian sống 24h (bằng với thời gian JWT của bạn)
            stringRedisTemplate.opsForValue().set(redisKey, token, java.time.Duration.ofHours(24));
            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "tokenType", "Bearer",
                    "id", user.getId(),
                    "username", user.getUsername(),
                    "email", user.getEmail(),
                    "roles", user.getRoles().getName(),
                    "xp", user.getXp(),
                    "streak", user.getStreak(),
                    "point", user.getPoint()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}
