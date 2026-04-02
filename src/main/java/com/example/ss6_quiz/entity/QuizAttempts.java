package com.example.ss6_quiz.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "quiz_attempts")
@SQLRestriction("is_deleted = false")
public class QuizAttempts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quizzes quiz;

    private Double score;
    boolean isPassed;
    @Column(name = "started_time", nullable = false)
    LocalDateTime startedTime;
    LocalDateTime endTime;
    boolean isDeleted = false;
}
