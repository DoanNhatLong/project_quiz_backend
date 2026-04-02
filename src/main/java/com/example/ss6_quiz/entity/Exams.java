package com.example.ss6_quiz.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Entity
@Table(name = "exams")
@SQLRestriction("is_deleted = false")
public class Exams {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Integer durationMinutes = 45;

    private Float passScorePercentage = 0.8f;

    private LocalDateTime createdAt = LocalDateTime.now();

    boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @OneToMany(mappedBy = "exams", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExamQuestion> examQuestions;
}