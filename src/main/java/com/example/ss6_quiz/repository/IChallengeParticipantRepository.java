package com.example.ss6_quiz.repository;

import com.example.ss6_quiz.entity.ChallengeParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IChallengeParticipantRepository extends JpaRepository<ChallengeParticipant, Long> {
    List<ChallengeParticipant> findByChallengeId(Long challengeId);

    boolean existsByChallengeIdAndUserId(Long challengeId, Long userId);
}
