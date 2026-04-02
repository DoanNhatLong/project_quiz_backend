package com.example.ss6_quiz.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_answers")
@SQLRestriction("is_deleted = false")
public class UserAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "attempt_id", nullable = false)
    private QuizAttempts attempt;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Questions question;

    @ManyToOne
    @JoinColumn(name = "answer_id", nullable = false)
    private Answers answer;

    @Column(name = "is_correct")
    private Boolean isCorrect;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
}