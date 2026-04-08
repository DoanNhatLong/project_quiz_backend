package com.example.ss6_quiz.controller.users;

import com.example.ss6_quiz.annotation.AdminActionLog;
import com.example.ss6_quiz.dto.QuestionUploadDto;
import com.example.ss6_quiz.dto.QuestionsRequestDto;
import com.example.ss6_quiz.dto.QuestionsResponseDto;
import com.example.ss6_quiz.entity.Questions;
import com.example.ss6_quiz.service.IQuestionsService;
import com.example.ss6_quiz.service.IQuizzesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/questions")
public class QuestionsController {

    @Autowired
    IQuestionsService questionsService;
    @Autowired
    IQuizzesService quizzesService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadQuestion(@RequestBody QuestionUploadDto dto) {
        try {
            questionsService.saveQuestion(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Lưu câu hỏi và đáp án thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lỗi: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<QuestionsResponseDto>> getAll() {
        return ResponseEntity.ok(questionsService.getAll());
    }

    @GetMapping("/quiz/{quizId}")
    public ResponseEntity<List<QuestionsResponseDto>> getByQuizId(@PathVariable Long quizId) {
        return ResponseEntity.ok(questionsService.getByQuizId(quizId));
    }


    @GetMapping("/{id}")
    public ResponseEntity<QuestionsResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(questionsService.getById(id));
    }

    @PostMapping
    public ResponseEntity<QuestionsResponseDto> create(@RequestBody QuestionsRequestDto dto) {
        return ResponseEntity.ok(questionsService.create(dto));
    }


    @PutMapping("/{id}")
    public ResponseEntity<QuestionsResponseDto> update(@PathVariable Long id,
                                                       @RequestBody QuestionsRequestDto dto) {
        return ResponseEntity.ok(questionsService.update(id, dto));
    }

    @PutMapping("/full/{id}")
    public void updateFull(@PathVariable Long id, @RequestBody Questions dto) {
        questionsService.saveFull(id, dto);
    }

    @AdminActionLog(action = "delete_question")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        questionsService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/detail/{quizId}")
    public List<Questions> getQuestionsByQuizId(@PathVariable Long quizId) {
        return quizzesService.findAllByQuizId(quizId);
    }
    @PostMapping("/upload-list")
    public ResponseEntity<String> importQuestions(@RequestBody List<QuestionUploadDto> dtoList) {
        try {

            if (dtoList == null || dtoList.isEmpty()) {
                return ResponseEntity.badRequest().body("Danh sách câu hỏi trống!");
            }
            questionsService.importAll(dtoList);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Import thành công " + dtoList.size() + " câu hỏi.");
        } catch (RuntimeException e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Đã xảy ra lỗi hệ thống khi import!");
        }
    }
}