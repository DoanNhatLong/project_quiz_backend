package com.example.ss6_quiz.service;

import com.example.ss6_quiz.entity.AdminLogs;
import com.example.ss6_quiz.projection.CountStatsProjection;

import java.util.List;

public interface IAdminLogsService {
    List<AdminLogs> getLogs(String actionType);

    CountStatsProjection getCountStats();

    void softDelete(Long id);
}
