package com.example.ss6_quiz.service;

import com.example.ss6_quiz.entity.ExamAttempts;
import com.example.ss6_quiz.projection.*;
import com.example.ss6_quiz.repository.IExamAttemptAnswerRepository;
import com.example.ss6_quiz.repository.IExamAttemptsRepository;
import com.example.ss6_quiz.repository.IExamsRepository;
import com.example.ss6_quiz.repository.IUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ExamAttemptsService implements IExamAttemptsService {
    @Autowired
    IExamAttemptsRepository examAttemptsRepository;
    @Autowired
    IExamsRepository examRepository;
    @Autowired
    IUsersRepository usersRepository;
    @Autowired
    IExamAttemptAnswerRepository examAttemptAnswerRepository;

    @Override
    public ExamAttempts createExamAttempt(Long examId, Long userId) {
        Optional<ExamAttempts> existingAttempt =
                examAttemptsRepository.findByExams_IdAndUser_Id(examId, userId);
        if (existingAttempt.isPresent()) {
            ExamAttempts attempt = existingAttempt.get();
            System.out.println(attempt.getStatus());
            if ("COMPLETED".equals(attempt.getStatus())) {
                throw new RuntimeException("Bạn đã nộp bài thi này rồi, không thể thi lại! 77");
            }

            return attempt;
        }
        ExamAttempts examAttempts = new ExamAttempts();
        examAttempts.setExams(examRepository.findById(examId).orElseThrow());
        examAttempts.setUser(usersRepository.findById(userId).orElseThrow());
        examAttempts.setDeleted(false);
        return examAttemptsRepository.save(examAttempts);
    }

    @Override
    public List<AnswerResponseProjection> findByAttemptId(Long attemptId) {
        return examAttemptAnswerRepository.findByAttemptId(attemptId);
    }

    @Override
    public List<ExamReviewProjection> findAllByUser_Id(Long userId) {
        return examAttemptsRepository.findAllByUser_Id(userId);
    }

    @Override
    @Transactional
    public double calculateScore(Long attemptId) {
        List<ExamGradingProjection> results = examAttemptsRepository.findGradingDataByAttemptId(attemptId);
        int totalCorrect = 0;
        int totalQuestions = results.size();

        for (ExamGradingProjection row : results) {
            String correct = row.getCorrectIds();
            String selected = row.getSelectedIds();

            if (selected != null && selected.equals(correct)) {
                totalCorrect++;
            }
        }
        double score = (double) totalCorrect / totalQuestions * 100;
        ExamAttempts attempt = examAttemptsRepository.findById(attemptId).orElseThrow();
        double passScore = attempt.getExams().getPassScorePercentage()*100;
        attempt.setTotalScore(score);
        attempt.setCompletedAt(LocalDateTime.now());
        attempt.setStatus("COMPLETED");
        attempt.setIsPassed(score >= passScore);
        examAttemptsRepository.save(attempt);
        return score;
    }

    @Override
    public ChallengerResultProjection findProjectionById(Long Id) {
        return examAttemptsRepository.findProjectionById(Id);
    }

    @Override
    public ExamAttempts findByIdAndStatus(Long id) {
        return examAttemptsRepository.findByIdAndStatus(id, "COMPLETED");
    }

    @Override
    public Optional<ExamAttemptProjection> findAttemptById(Long id) {
        return examAttemptsRepository.findAttemptById(id);
    }

    @Override
    public List<ExamDetailProjection> findExamDetail(Long examId){
        return examAttemptsRepository.findExamDetail(examId);
    }
}
