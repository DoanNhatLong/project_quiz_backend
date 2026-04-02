package com.example.ss6_quiz.service;

import com.example.ss6_quiz.dto.QuizResultResponseDto;
import com.example.ss6_quiz.entity.*;
import com.example.ss6_quiz.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class QuizAttemptsService implements IQuizAttemptsService {
    @Autowired
    private IQuizAttemptsRepository attemptRepository;
    @Autowired
    private IUsersRepository usersRepository;
    @Autowired
    private IQuizzesRepository quizzesRepository;
    @Autowired
    private IUserAnswerRepository userAnswerRepository;
    @Autowired
    private IAnswerRepository answersRepository;
    @Autowired
    private IQuestionsRepository questionsRepository;

    @Override
    public QuizAttempts startAttempt(QuizAttempts attempt) {
        attempt.setStartedTime(LocalDateTime.now());
        return attemptRepository.save(attempt);
    }

    @Override
    public QuizAttempts submitAttempt(Long id, Double score, boolean isPassed) {
        QuizAttempts attempt = attemptRepository.findById(id).orElseThrow();
        attempt.setScore(score);
        attempt.setPassed(isPassed);
        attempt.setEndTime(LocalDateTime.now());
        return attemptRepository.save(attempt);
    }

    @Override
    public List<QuizAttempts> getHistoryByUser(Long userId) {
        return attemptRepository.findAllByUserId(userId);
    }

    @Override
    public QuizAttempts startNewAttempt(Long userId, Long quizId) {
        Users user = usersRepository.findById(userId).orElseThrow();
        Quizzes quiz = quizzesRepository.findById(quizId).orElseThrow();
        QuizAttempts attempt = new QuizAttempts();
        attempt.setUser(user);
        attempt.setQuiz(quiz);
        attempt.setStartedTime(LocalDateTime.now());
        attempt.setEndTime(null);
        attempt.setScore(null);
        attempt.setPassed(false);
        attempt.setDeleted(false);
        return attemptRepository.save(attempt);
    }


    @Override
    public QuizResultResponseDto submitQuiz(Long attemptId, Map<Long, List<Long>> answers) {

        // 1. Lấy attempt
        QuizAttempts attempt = attemptRepository.findById(attemptId)
                .orElseThrow(() -> new RuntimeException("Attempt không tồn tại"));

        int correctCount = 0;
        int totalQuestions = 10;

        List<UserAnswer> userAnswersToSave = new ArrayList<>();

        // 2. Duyệt từng câu
        for (Map.Entry<Long, List<Long>> entry : answers.entrySet()) {

            Long questionId = entry.getKey();
            List<Long> userAnswerIds = entry.getValue();

            // lấy tất cả đáp án đúng của câu hỏi
            List<Answers> correctAnswers = answersRepository
                    .findByQuestion_IdAndIsCorrectTrue(questionId);

            List<Long> correctIds = correctAnswers.stream()
                    .map(Answers::getId)
                    .toList();

            // check đúng
            boolean isCorrect =
                    correctIds.size() == userAnswerIds.size()
                    && correctIds.containsAll(userAnswerIds);

            if (isCorrect) correctCount++;

            // 3. Lưu từng answer user chọn
            for (Long ansId : userAnswerIds) {
                UserAnswer ua = new UserAnswer();
                ua.setAttempt(attempt);
                ua.setQuestion(questionsRepository.findById(questionId).orElse(null));
                ua.setAnswer(answersRepository.findById(ansId).orElse(null));
                ua.setIsCorrect(isCorrect);
                ua.setIsDeleted(false);

                userAnswersToSave.add(ua);
            }
        }

        // save batch
        userAnswerRepository.saveAll(userAnswersToSave);

        // 4. Tính điểm
        double score = (double) correctCount / totalQuestions;

        attempt.setScore(score);
        attempt.setPassed(score >= attempt.getQuiz().getPassScore());
        attempt.setEndTime(LocalDateTime.now());

        attemptRepository.save(attempt);

        return new QuizResultResponseDto(correctCount, totalQuestions, score);
    }
}

