package com.example.ss6_quiz.service;

import com.example.ss6_quiz.dto.ChallengesRequestDto;
import com.example.ss6_quiz.entity.Challenges;
import com.example.ss6_quiz.projection.ChallengesDetailProjection;

import java.util.List;
import java.util.Optional;

public interface IChallengesService {
    Challenges createChallenger(ChallengesRequestDto challenges);
    String joinChallenger(Long challengerId, Long userId, String inputCode);
    List<Challenges> getWaitingChallengers();
    List<Challenges> getAllChallenges();

    ChallengesDetailProjection getChallengeByIdUser(Long challengeId);
    List<ChallengesDetailProjection> findChallengeDetail();
    Challenges getChallengeById(Long challengeId);
}
