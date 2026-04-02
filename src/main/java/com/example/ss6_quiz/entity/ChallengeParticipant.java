package com.example.ss6_quiz.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "challenge_participants")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ChallengeParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenges challenge;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    private Boolean isOnline = false;
    private Boolean isFocus = true;
    private Integer violationCount = 0;
    private Boolean isDisqualified = false;
    private Boolean submitted = false;
    private LocalDateTime startedAt;
}