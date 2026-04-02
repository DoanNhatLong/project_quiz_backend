package com.example.ss6_quiz.repository;

import com.example.ss6_quiz.entity.Challenges;
import com.example.ss6_quiz.projection.ChallengesDetailProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IChallengeRepository extends JpaRepository<Challenges, Long> {
    List<Challenges> findAllByStatus(Challenges.ChallengeStatus status);

    @Query(value = "select c.id, c.title, c.access_code, c.exam_id, c.duration_minutes, c.start_time " +
                   "from challenges c " +
                   "where c.status ='waiting'", nativeQuery = true)
    List<ChallengesDetailProjection> findChallengeDetail();

    @Query(value = "select c.id, c.title, c.access_code, c.exam_id, c.duration_minutes, c.start_time " +
                   "from challenges c " +
                   "where c.id = :challengeId", nativeQuery = true)
    Optional<ChallengesDetailProjection> getChallengeById(@Param("challengeId") Long challengeId);
}
