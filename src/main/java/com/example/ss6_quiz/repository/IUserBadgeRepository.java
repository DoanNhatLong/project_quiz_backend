package com.example.ss6_quiz.repository;

import com.example.ss6_quiz.entity.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUserBadgeRepository extends JpaRepository<UserBadge,Long> {
    @Query("SELECT ub FROM UserBadge ub JOIN FETCH ub.badge WHERE ub.userId = :userId AND ub.isDeleted = 0")
    List<UserBadge> findAllWithBadgeByUserId(@Param("userId") Long userId);
}
