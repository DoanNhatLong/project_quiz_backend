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
@SQLRestriction("is_deleted = false")
@Table(name = "quizzes")
public class Quizzes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private Float passScore = 0.5f;

    private LocalDateTime createdAt = LocalDateTime.now();

    boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subjects subject;

    @Column(name = "level")
    private Integer level = 1;


}