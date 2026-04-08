package com.example.ss6_quiz.controller.admin;

import com.example.ss6_quiz.annotation.AdminActionLog;
import com.example.ss6_quiz.dto.ChallengesRequestDto;
import com.example.ss6_quiz.entity.Challenges;
import com.example.ss6_quiz.entity.Questions;
import com.example.ss6_quiz.service.IChallengesService;
import com.example.ss6_quiz.service.IExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/challenges")
public class ChallengeController {
    @Autowired
    private IChallengesService challengesService;
    @Autowired
    private IExamService examService;

    @AdminActionLog(action = "create_exam")
    @PostMapping("/create")
    public ResponseEntity<?> createChallenge(@RequestBody ChallengesRequestDto challenge) {
        try {
            Challenges newChallenge = challengesService.createChallenger(challenge);
            return new ResponseEntity<>(newChallenge, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi tạo thử thách: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{examId}/questions")
    public ResponseEntity<Page<Questions>> getExamQuestions(
            @PathVariable Long examId,
            @PageableDefault(size = 10) Pageable pageable) {

        Page<Questions> questionsPage = examService.getExamQuestions(examId, pageable);
        return ResponseEntity.ok(questionsPage);
    }

    @GetMapping()
    public ResponseEntity<List<Challenges>> getAllChallenges() {
        List<Challenges> challengesList = challengesService.getAllChallenges();
        return ResponseEntity.ok(challengesList);
    }

    @GetMapping("{challengeId}")
    public ResponseEntity<Challenges> getChallengeById(@PathVariable Long challengeId) {
        Challenges challenge = challengesService.getChallengeById(challengeId);
        return ResponseEntity.ok(challenge);
    }

    @GetMapping("/check/{userId}")
    public List<Challenges> checkExam(@PathVariable Long userId){
        return challengesService.findAllByUserId(userId);
    }

}
