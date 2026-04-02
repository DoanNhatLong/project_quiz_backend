package com.example.ss6_quiz.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import java.time.LocalDateTime;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Entity
@Table(name = "exam_attempts")
@SQLRestriction("is_deleted = false")
public class ExamAttempts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "exam_id", nullable = false)
    private Exams exams;

    private Double totalScore;
    private Boolean isPassed;

    private LocalDateTime startedAt = LocalDateTime.now();
    private LocalDateTime completedAt;
    private String status = "IN_PROGRESS";

    boolean isDeleted = false;
}