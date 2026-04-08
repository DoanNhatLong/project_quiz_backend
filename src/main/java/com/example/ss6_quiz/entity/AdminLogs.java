package com.example.ss6_quiz.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "admin_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("is_deleted = false")
public class AdminLogs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "admin_username", nullable = false, length = 100)
    private String adminUsername;

    @Column(name = "action_type", nullable = false)
    private String actionType;

    @Column(name = "action_detail", columnDefinition = "json")
    private String actionDetail;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    public AdminLogs(Long adminId, String adminUsername, String actionType, String actionDetail) {
        this.adminUsername = adminUsername;
        this.actionType = actionType;
        this.actionDetail = actionDetail;
        this.createdAt = LocalDateTime.now();
    }
}