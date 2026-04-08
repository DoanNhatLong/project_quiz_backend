package com.example.ss6_quiz.repository;

import com.example.ss6_quiz.entity.Challenges;
import com.example.ss6_quiz.projection.ChallengesDetailProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

    @Modifying
    @Transactional
    @Query("UPDATE Challenges c SET c.status = 'STARTED' " +
           "WHERE c.id = :id " +
           "AND c.status = 'WAITING' ")
    int startChallengeIfTimeReached(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE challenges SET status = 'STARTED' " +
                   "WHERE status = 'WAITING' " +
                   "AND is_deleted = false " +
                   "AND start_time <= NOW()",
            nativeQuery = true)
    int startAllReachableChallenges();

    @Modifying
    @Transactional
    @Query(value = "UPDATE challenges SET status = 'ENDED' " +
                   "WHERE status = 'STARTED' " +
                   "AND DATE_ADD(start_time, INTERVAL duration_minutes MINUTE) <= CURRENT_TIMESTAMP",
            nativeQuery = true)
    int endExpiredChallenges();

    @Query("SELECT c FROM Challenges c WHERE c.user.id = :userId AND c.isDeleted = false ")
    List<Challenges> findAllByUserId(Long userId);
}
