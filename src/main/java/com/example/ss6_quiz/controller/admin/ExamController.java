package com.example.ss6_quiz.controller.admin;

import com.example.ss6_quiz.annotation.AdminActionLog;
import com.example.ss6_quiz.dto.ExamRequestDto;
import com.example.ss6_quiz.entity.Exams;
import com.example.ss6_quiz.entity.Questions;
import com.example.ss6_quiz.service.IExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/exams")
public class ExamController {
    @Autowired
    private IExamService examService;

    @AdminActionLog (action = "create_exam")
    @PostMapping("/create")
    public ResponseEntity<?> createExam(@RequestBody ExamRequestDto dto) {
        try {
            Exams createdExam = examService.createExam(dto);

            return new ResponseEntity<>(createdExam, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi hệ thống: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<Exams>> getAllExams() {
        return ResponseEntity.ok(examService.getAllExams());
    }

    @GetMapping("/{examId}")
    public ResponseEntity<?> getExam(@PathVariable Long examId) {
        try {
            Exams exam = examService.getExamById(examId);
            return new ResponseEntity<>(exam, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi hệ thống: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/{examId}/questions")
    public ResponseEntity<Page<Questions>> getExamQuestions(
            @PathVariable Long examId,
            @PageableDefault(size = 10) Pageable pageable) {

        Page<Questions> questionsPage = examService.getExamQuestions(examId, pageable);
        return ResponseEntity.ok(questionsPage);
    }

    @DeleteMapping("/{examId}")
    public ResponseEntity<String> deleteExam(@PathVariable Long examId) {
        examService.deleteExam(examId);
        return ResponseEntity.ok("Đã xóa bài thi thành công!");
    }

}
