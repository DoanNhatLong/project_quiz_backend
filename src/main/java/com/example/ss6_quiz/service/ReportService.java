package com.example.ss6_quiz.service;

import com.example.ss6_quiz.repository.IReportsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportService implements IReportService {
    @Autowired
    private IReportsRepository reportsRepository;
}
