package com.example.ss6_quiz.controller.users;

import com.example.ss6_quiz.dto.QuizRequestDto;
import com.example.ss6_quiz.entity.Questions;
import com.example.ss6_quiz.entity.Quizzes;
import com.example.ss6_quiz.entity.Subjects;
import com.example.ss6_quiz.service.IQuestionsService;
import com.example.ss6_quiz.service.IQuizzesService;
import com.example.ss6_quiz.service.ISubjectsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quizzes")
public class QuizzesController {
    @Autowired
    private IQuizzesService quizzesService;
    @Autowired
    private IQuestionsService questionsService;
    @Autowired
    private ISubjectsService subjectsService;

    @GetMapping
    public List<Quizzes> getAll(
            @RequestParam(required = false) String language,
            @RequestParam(required = false) Integer level
    ) {
        if (language != null && level != null) {
            return quizzesService.findAllBySubjectAndLevel(language, level);
        } else if (language != null) {
            return quizzesService.findAllBySubject(subjectsService.findByName(language));
        }
        return quizzesService.getAllQuizzes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Quizzes> getById(@PathVariable Long id) {
        return ResponseEntity.ok(quizzesService.getQuizById(id));
    }

    @PostMapping
    public Quizzes create(@RequestBody Quizzes quiz) {
        return quizzesService.createQuiz(quiz);
    }

    @GetMapping("/{id}/play")
    public List<Questions> test(@PathVariable Long id) {
        return questionsService.findRandom10ByQuizId(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Quizzes> update(@PathVariable Long id, @RequestBody Quizzes quizDetails) {
        return ResponseEntity.ok(quizzesService.updateQuiz(id, quizDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        quizzesService.deleteQuiz(id);
        return ResponseEntity.ok("Đã xóa bộ câu hỏi thành công!");
    }

    @PostMapping("/create")
    public ResponseEntity<Quizzes> createQuiz(@RequestBody QuizRequestDto dto) {
        Quizzes newQuiz = quizzesService.createQuiz(dto);
        return new ResponseEntity<>(newQuiz, HttpStatus.CREATED);
    }

    @GetMapping("/practice")
    public ResponseEntity<List<Quizzes>> getPracticeQuizzes(
            @RequestParam("language") String language,
            @RequestParam("level") Integer level) {
        try {
            List<Quizzes> list = quizzesService.findAllBySubjectAndLevel(language, level);
            if (list.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(list);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
