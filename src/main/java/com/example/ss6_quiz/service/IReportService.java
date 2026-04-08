package com.example.ss6_quiz.service;

import com.example.ss6_quiz.dto.ReportRequestDto;
import com.example.ss6_quiz.entity.Reports;

import java.util.List;

public interface IReportService {
    void saveReport(ReportRequestDto dto);
    Reports findByAttemptId(Long attemptId);
    List<Reports> findAllByStatusIs(String status);

    int solveReport(Long id);

    void softDelete(Long id);
}
