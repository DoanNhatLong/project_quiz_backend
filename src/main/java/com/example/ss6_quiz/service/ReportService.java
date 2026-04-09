package com.example.ss6_quiz.service;

import com.example.ss6_quiz.dto.ReportRequestDto;
import com.example.ss6_quiz.entity.Reports;
import com.example.ss6_quiz.repository.IReportsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportService implements IReportService {
    @Autowired
    private IReportsRepository reportsRepository;

    @Override
    public void saveReport(ReportRequestDto dto) {
        Reports report = new Reports();
        report.setAttemptId(dto.attemptId());
        report.setExamId(dto.examId());
        report.setStartTime(dto.startTime());
        report.setMessage(dto.message());
        report.setStatus(dto.status());
        report.setCreatedAt(LocalDateTime.now());
        report.setDeleted(false);
        reportsRepository.save(report);
    }

    @Override
    public Reports findByAttemptId(Long attemptId) {
        return reportsRepository.findByAttemptId(attemptId);
    }

    @Override
    public List<Reports> findAllByStatusIs(String status) {
        return reportsRepository.findAllByStatusIs(status);
    }

    @Override
    public int solveReport(Long id){
        return reportsRepository.solveReport(id);
    }
    @Override
    public void softDelete(Long id){
        reportsRepository.softDelete(id);
    }


}
