package com.example.ss6_quiz.controller.users;

import com.example.ss6_quiz.dto.UpdateProfileRequestDto;
import com.example.ss6_quiz.dto.UserSystemDto;
import com.example.ss6_quiz.entity.ExamAttempts;
import com.example.ss6_quiz.entity.Users;
import com.example.ss6_quiz.projection.ChallengerResultProjection;
import com.example.ss6_quiz.projection.ExamAttemptProjection;
import com.example.ss6_quiz.projection.ExamReviewProjection;
import com.example.ss6_quiz.service.IExamAttemptsService;
import com.example.ss6_quiz.service.IExamSnapshotService;
import com.example.ss6_quiz.service.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequestMapping("/users")
@RestController
public class UserController {
    @Autowired
    IUsersService usersService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    IExamAttemptsService examAttemptsService;
    @Autowired
    IExamSnapshotService examSnapshotService;

    // 1. Lấy danh sách (Read)
    @GetMapping
    public List<Users> getAllUsers() {
        System.out.println("docker");
        return usersService.getAllUsers();
    }

    // 2. Lấy 1 người dùng theo ID (Read)
    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(usersService.getUserById(id));
    }

    // 3. Tạo mới (Create)
    @PostMapping
    public Users createUser(@RequestBody Users user) {
        return usersService.createUser(user);
    }

    // 4. Cập nhật (Update)
    @PutMapping("/{id}")
    public ResponseEntity<Users> updateUser(@PathVariable Long id, @RequestBody Users userDetails) {
        return ResponseEntity.ok(usersService.updateUser(id, userDetails));
    }
    
    // 5. Xóa (Delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        usersService.deleteUser(id);
        return ResponseEntity.ok("Đã xóa người dùng thành công");
    }

    @PutMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@RequestBody UpdateProfileRequestDto request) {
        Users user = usersService.findById(request.id());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "User not found"));
        }

        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Mật khẩu cũ không chính xác"));
        }

        user.setUsername(request.username());

        if (request.newPassword() != null && !request.newPassword().isBlank()) {
            String encodedPassword = passwordEncoder.encode(request.newPassword());
            user.setPassword(encodedPassword);
        }

        Users updatedUser = usersService.updateUser(user.getId(), user);
        return ResponseEntity.ok(updatedUser);
    }
    @GetMapping("/all-users")
    public ResponseEntity<List<UserSystemDto>> getAllUsersSystemDto() {
        return ResponseEntity.ok(usersService.findAllUserSystemDto());
    }

    @GetMapping("/admin")
    public ResponseEntity<Page<Users>> listUsers(
            @RequestParam(required = false) String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("xp").descending());

        Page<Users> userPage = usersService.findAllUsers(pageable, username);
        return ResponseEntity.ok(userPage);
    }

    @PostMapping("/add-xp")
    public ResponseEntity<?> addXp(@RequestBody Map<String, Object> payload) {
        Long quizAttemptsId = Long.valueOf(payload.get("quizAttemptsId").toString());
        Integer xp = Integer.valueOf(payload.get("xp").toString());
        usersService.addXp(quizAttemptsId, xp);
        return ResponseEntity.ok("Đã cộng điểm thành công!");
    }

    @GetMapping("challenges/{userId}")
    public ResponseEntity<List<ExamReviewProjection>> getUserChallenges(@PathVariable Long userId) {
        return ResponseEntity.ok(examAttemptsService.findAllByUser_Id(userId));
    }

    @GetMapping("scores/{attemptId}")
    public ResponseEntity<Double> getUserScore(@PathVariable Long attemptId) {
        return ResponseEntity.ok(examAttemptsService.calculateScore(attemptId));
    }

    @GetMapping("result/{attemptId}")
    public ResponseEntity<ChallengerResultProjection> getUserResult(@PathVariable Long attemptId) {
        return ResponseEntity.ok(examAttemptsService.findProjectionById(attemptId));
    }

    @GetMapping("snapshot/{attemptId}")
    public ResponseEntity<?> getSnapshot(@PathVariable Long attemptId) {
        Optional<String> snapshotData = examSnapshotService.findRawDataByAttemptId(attemptId);
        return snapshotData.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Snapshot not found"));
    }

    @GetMapping("attempt/{attemptId}")
    public ResponseEntity<Optional<ExamAttemptProjection>> getDetail(@PathVariable Long attemptId) {
        return ResponseEntity.ok(examAttemptsService.findAttemptById(attemptId));
    }


}
