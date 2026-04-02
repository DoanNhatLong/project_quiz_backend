package com.example.ss6_quiz.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Entity
@Table(name = "answers")
@SQLRestriction("is_deleted = false")
public class Answers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    @JsonIgnore 
    private Questions question;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "is_correct")
    private boolean isCorrect = false;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;
}