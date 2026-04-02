package com.example.ss6_quiz.controller.admin;

import com.example.ss6_quiz.entity.Subjects;
import com.example.ss6_quiz.service.IQuizzesService;
import com.example.ss6_quiz.service.ISubjectsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/subjects")
public class SubjectController {
    @Autowired
    private ISubjectsService subjectsService;
    @Autowired
    private IQuizzesService quizzesService;

    @GetMapping
    public List<Subjects> getAllSubjects() {
        return subjectsService.findAll();
    }

    @PostMapping("/create")
    public Subjects createSubject(@RequestBody Map<String, String> payload) {
        String name = payload.get("name");
        subjectsService.create(name);
        return subjectsService.findByName(name);
    }

    @GetMapping("/quizzes/{quizId}")
    public Long getSubjectByQuizId(@PathVariable Long quizId) {
        return quizzesService.getSubjectIdByQuizId(quizId);}
}
