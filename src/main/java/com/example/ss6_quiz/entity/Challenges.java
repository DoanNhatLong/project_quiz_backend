package com.example.ss6_quiz.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import java.time.LocalDateTime;

@Entity
@Table(name = "challenges")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@SQLRestriction("is_deleted = false")
public class Challenges {

    public enum ChallengeStatus {
        WAITING, STARTED, ENDED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne
    @JoinColumn(name = "exam_id", nullable = false)
    private Exams exam;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(name = "access_code", nullable = false)
    private String accessCode;

    @Enumerated(EnumType.STRING)
    private ChallengeStatus status = ChallengeStatus.WAITING;

    private Integer durationMinutes;

    private LocalDateTime startTime;

    @Column(name = "allow_rejoin")
    private Boolean allowRejoin = true;

    private LocalDateTime createdAt = LocalDateTime.now();

    private Boolean isDeleted = false;
}