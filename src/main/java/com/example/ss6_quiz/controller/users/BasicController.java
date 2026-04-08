package com.example.ss6_quiz.controller.users;

import com.example.ss6_quiz.dto.ReportRequestDto;
import com.example.ss6_quiz.entity.Badges;
import com.example.ss6_quiz.entity.Reports;
import com.example.ss6_quiz.entity.UserBadge;
import com.example.ss6_quiz.projection.ExamDetailProjection;
import com.example.ss6_quiz.service.IExamAttemptsService;
import com.example.ss6_quiz.service.IExamService;
import com.example.ss6_quiz.service.IReportService;
import com.example.ss6_quiz.service.IUserBadgeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class BasicController {
    @Autowired
    IReportService reportService;
    @Autowired
    IExamService examService;
    @Autowired
    IExamAttemptsService examAttemptsService;
    @Autowired
    IUserBadgeService userBadgeService;

    @PostMapping("/report")
    void createReport(@RequestBody ReportRequestDto dto){
        reportService.saveReport(dto);
    }

    @GetMapping("/report/{attemptId}")
    public Reports getReport(@PathVariable Long attemptId){
        log.info("Test LOG INFO - ID: {}", attemptId);
        return reportService.findByAttemptId(attemptId);
    }

    @GetMapping("/report/get/{status}")
    public List<Reports> getAllReport(@PathVariable String status){
        return reportService.findAllByStatusIs(status.toUpperCase());
    }

    @GetMapping("/duration/{examId}")
    public Integer getDuration(@PathVariable Long examId){
        return examService.findDurationByExamId(examId);
    }

    @GetMapping("/check/{examId}")
    List<ExamDetailProjection> checkExam(@PathVariable Long examId){
        return examAttemptsService.findExamDetail(examId);
    }

    @GetMapping("/badges/{userId}")
    List<UserBadge> findBadge(@PathVariable Long userId){
        return userBadgeService.findAllWithBadgeByUserId(userId);
    }
}
