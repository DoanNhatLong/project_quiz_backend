package com.example.ss6_quiz.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Entity
@Table(name = "exam_questions")
@SQLRestriction("is_deleted = false")
public class ExamQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "exam_id", nullable = false)
    @JsonIgnore
    private Exams exams;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Questions question;

    boolean isDeleted = false;
}