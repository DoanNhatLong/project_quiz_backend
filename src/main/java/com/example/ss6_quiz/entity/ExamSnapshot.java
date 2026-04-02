package com.example.ss6_quiz.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "exam_snapshots")
public class ExamSnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "exam_id")
    private Long examId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "snapshot_data", columnDefinition = "JSON")
    private String snapshotData;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
