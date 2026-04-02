package com.example.ss6_quiz.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "exam_attempt_answers",
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_attempt_question", columnNames = {"attempt_id", "question_id"})
        })
public class ExamAttemptAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "attempt_id", nullable = false)
    private Long attemptId;

    @Column(name = "question_id", nullable = false)
    private Long questionId;

    @Column(name = "selected_option_id")
    private String selectedOptionId;

    @Column(name = "is_correct")
    private Boolean isCorrect;

}
