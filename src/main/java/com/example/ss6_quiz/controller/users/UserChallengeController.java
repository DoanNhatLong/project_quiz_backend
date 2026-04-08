package com.example.ss6_quiz.controller.users;

import com.example.ss6_quiz.dto.JoinChallengeRequest;
import com.example.ss6_quiz.dto.TempExamRequestDto;
import com.example.ss6_quiz.entity.Challenges;

import com.example.ss6_quiz.entity.ExamAttempts;
import com.example.ss6_quiz.projection.AnswerResponseProjection;
import com.example.ss6_quiz.projection.ChallengesDetailProjection;
import com.example.ss6_quiz.projection.ExamQuestionProjection;
import com.example.ss6_quiz.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/challenges")
public class UserChallengeController {

    @Autowired
    private IChallengesService challengesService;
    @Autowired
    private IExamService examService;
    @Autowired
    private IExamSnapshotService examSnapshotService;
    @Autowired
    private IExamAttemptsService examAttemptsService;
    @Autowired
    private ExamTimerService examTimerService;


    @GetMapping("/waiting")
    public ResponseEntity<List<Challenges>> getWaitingChallenges() {
        return ResponseEntity.ok(challengesService.getWaitingChallengers());
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<?> joinChallenge(
            @PathVariable("id") Long challengeId,
            @RequestBody JoinChallengeRequest request) {

        try {
            String result = challengesService.joinChallenger(
                    challengeId,
                    request.userId(),
                    request.accessCode()
            );

            if ("SUCCESS".equals(result)) {
                return ResponseEntity.ok("Bạn đã tham gia phòng thành công!");
            } else {
                return ResponseEntity.badRequest().body(result);
            }

        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi hệ thống: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{challengeId}")
    public ResponseEntity<ChallengesDetailProjection> getChallengeById(@PathVariable Long challengeId) {
        ChallengesDetailProjection challenge = challengesService.getChallengeByIdUser(challengeId);
        return ResponseEntity.ok(challenge);

    }

    @GetMapping("/exam/{examId}")
    public List<ExamQuestionProjection> getExamQuestions(@PathVariable Long examId) {
        return examService.getAllExamQuestions(examId);
    }

    @PostMapping("/snapshot")
    public ResponseEntity<?> createSnapshot(@RequestBody Map<String, Object> payload) {
        Long examId = Long.valueOf(payload.get("examId").toString());
        Long userId = Long.valueOf(payload.get("userId").toString());
        Object data = payload.get("data");

        examSnapshotService.saveSnapshot(examId, userId, data);
        return ResponseEntity.ok("Snapshot created");
    }

    @GetMapping("/find")
    public ResponseEntity<?> findChallengeDetail() {
        List<ChallengesDetailProjection> challengeDetail = challengesService.findChallengeDetail();
        return ResponseEntity.ok(challengeDetail);
    }

    @PostMapping("/attempts/{examId}/{userId}/{challengeId}")
    public ExamAttempts createAttempt(
            @PathVariable Long examId,
            @PathVariable Long userId,
            @PathVariable Long challengeId
    ) {
        ExamAttempts attempt = examAttemptsService.createExamAttempt(examId, userId);
        Integer duration = attempt.getExams().getDurationMinutes();
        examTimerService.startExamTimer(attempt.getId().toString(), duration);
//        challengesService.startChallengeIfTimeReached(challengeId);
        return attempt;

    }

    @PostMapping("/tempExam")
    @Transactional
    public ResponseEntity<?> createTempExam(@RequestBody TempExamRequestDto payload) {
        examService.createTempExam(payload);
//        examAttemptsService.calculateScore(Long.parseLong(payload.attemptId()));
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/{attemptId}/answers")
    public ResponseEntity<List<AnswerResponseProjection>> getExamAnswers(@PathVariable Long attemptId) {
        return ResponseEntity.ok(examAttemptsService.findByAttemptId(attemptId));
    }

    @PostMapping("/{attemptId}/calculate")
    public void calculateScore(@PathVariable Long attemptId) {
        examAttemptsService.calculateScore(attemptId);
    }

    @GetMapping("/review/{attemptId}")
    public ExamAttempts reviewExam(@PathVariable Long attemptId) {
        return examAttemptsService.findByIdAndStatus(attemptId);
    }
}