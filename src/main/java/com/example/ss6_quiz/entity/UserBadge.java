package com.example.ss6_quiz.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "user_badges",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "badge_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserBadge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "badge_id", nullable = false)
    private Badges badge;

    @Column(name = "earned_at", insertable = false, updatable = false)
    private LocalDateTime earnedAt;

    @Column(name = "is_deleted", nullable = false)
    private Integer isDeleted = 0;
}
