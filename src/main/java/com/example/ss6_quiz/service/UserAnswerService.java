package com.example.ss6_quiz.service;

import com.example.ss6_quiz.entity.UserAnswer;
import com.example.ss6_quiz.repository.IUserAnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAnswerService implements IUserAnswerService {
    @Autowired
    IUserAnswerRepository userAnswerRepository;

    @Override
    public List<UserAnswer> findByAttempt_Id(Long attemptId) {
        return userAnswerRepository.findByAttempt_Id(attemptId);
    }
}
