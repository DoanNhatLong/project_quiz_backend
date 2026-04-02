package com.example.ss6_quiz.controller.admin.question;

import com.example.ss6_quiz.dto.QuestionsRequestDto;
import com.example.ss6_quiz.dto.QuestionsResponseDto;
import com.example.ss6_quiz.entity.Questions;
import com.example.ss6_quiz.service.IQuestionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/questions")
public class QuestionController {
    @Autowired
    private IQuestionsService questionsService;

    @GetMapping("/{quizId}")
    public Page<Questions> getQuestions(
            @PathVariable Long quizId,
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(required = false) String content
    ) {
        return questionsService.getAllQuestionExam(pageable, content, quizId);
    }

    @PostMapping
    public QuestionsResponseDto createQuestion(@RequestBody QuestionsRequestDto dto) {
        return questionsService.create(dto);
    }

    @PutMapping("/{id}")
    public QuestionsResponseDto updateQuestion(@PathVariable Long id, @RequestBody QuestionsRequestDto dto) {
        return questionsService.update(id, dto);
    }

    @GetMapping("/question/{id}")
    public Questions getQuestion(@PathVariable Long id) {
        return questionsService.getQuestionById(id);
    }


}
