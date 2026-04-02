package com.example.ss6_quiz.controller.users;

import com.example.ss6_quiz.dto.QuizResultResponseDto;
import com.example.ss6_quiz.entity.QuizAttempts;
import com.example.ss6_quiz.entity.UserAnswer;
import com.example.ss6_quiz.service.IQuizAttemptsService;
import com.example.ss6_quiz.service.IUserAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/quiz-attempts")
public class QuizAttemptsController {
    @Autowired
    private IQuizAttemptsService attemptService;
    @Autowired
    private IUserAnswerService userAnswerService;

    @PostMapping("/start")
    public ResponseEntity<?> start(@RequestBody Map<String, Long> payload) {

        Long quizId = payload.get("quizId");
        Long userId = payload.get("userId");

        QuizAttempts newAttempt = attemptService.startNewAttempt(userId, quizId);
        return ResponseEntity.ok(newAttempt);
    }

    @PatchMapping("/{id}/submit")
    public ResponseEntity<QuizAttempts> submit(
            @PathVariable Long id,
            @RequestParam Double score,
            @RequestParam boolean isPassed) {
        return ResponseEntity.ok(attemptService.submitAttempt(id, score, isPassed));
    }

    @GetMapping("/user/{userId}")
    public List<QuizAttempts> getHistory(@PathVariable Long userId) {
        return attemptService.getHistoryByUser(userId);
    }

    @PostMapping("/submit")
    public QuizResultResponseDto submitQuiz(@RequestBody Map<String, Object> body) {

        Long attemptId = Long.valueOf(body.get("attemptId").toString());

        Map<String, List<Integer>> rawAnswers =
                (Map<String, List<Integer>>) body.get("answers");

        // convert String -> Long
        Map<Long, List<Long>> answers = new HashMap<>();

        for (Map.Entry<String, List<Integer>> entry : rawAnswers.entrySet()) {
            Long questionId = Long.valueOf(entry.getKey());
            List<Long> ansIds = entry.getValue().stream()
                    .map(Long::valueOf)
                    .toList();

            answers.put(questionId, ansIds);
        }

        return attemptService.submitQuiz(attemptId, answers);
    }

    @GetMapping("/{attemptId}/answers")
    public ResponseEntity<List<UserAnswer>> getAnswers(@PathVariable Long attemptId) {
        return ResponseEntity.ok(userAnswerService.findByAttempt_Id(attemptId));
    }

}
