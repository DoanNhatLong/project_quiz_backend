package com.example.ss6_quiz.service;

import com.example.ss6_quiz.entity.UserBadge;

import java.util.List;

public interface IUserBadgeService {
    List<UserBadge> findAllWithBadgeByUserId(Long userId);
}
