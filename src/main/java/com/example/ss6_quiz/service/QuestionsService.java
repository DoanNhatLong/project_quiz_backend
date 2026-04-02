package com.example.ss6_quiz.service;

import com.example.ss6_quiz.dto.QuestionUploadDto;
import com.example.ss6_quiz.dto.QuestionsRequestDto;
import com.example.ss6_quiz.dto.QuestionsResponseDto;
import com.example.ss6_quiz.entity.Answers;
import com.example.ss6_quiz.entity.Questions;
import com.example.ss6_quiz.entity.Quizzes;
import com.example.ss6_quiz.repository.IQuestionsRepository;
import com.example.ss6_quiz.repository.IQuizzesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionsService implements IQuestionsService {
    @Autowired
    private IQuestionsRepository questionsRepository;

    @Autowired
    private IQuizzesRepository quizzesRepository;

    private QuestionsResponseDto toDTO(Questions q) {
        return new QuestionsResponseDto(
                q.getId(),
                q.getQuiz().getId(),
                q.getQuiz().getTitle(),
                q.getContent(),
                q.getType(),
                q.getOrderIndex(),
                q.getCreatedAt()
        );
    }

    @Override
    public List<QuestionsResponseDto> getAll() {
        return questionsRepository.findAll()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<QuestionsResponseDto> getByQuizId(Long quizId) {
        return questionsRepository.findByQuizId(quizId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public QuestionsResponseDto getById(Long id) {
        Questions q = questionsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found"));
        return toDTO(q);
    }

    @Override
    public QuestionsResponseDto create(QuestionsRequestDto dto) {
        Quizzes quiz = quizzesRepository.findById(dto.quizId())
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        Questions q = new Questions();
        q.setQuiz(quiz);
        q.setContent(dto.content());
        q.setType(dto.type() != null ? dto.type() : Questions.QuestionType.single);
        q.setOrderIndex(dto.orderIndex());
        return toDTO(questionsRepository.save(q));
    }

    @Override
    public QuestionsResponseDto update(Long id, QuestionsRequestDto dto) {
        Questions q = questionsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        if (dto.quizId() != null) {
            Quizzes quiz = quizzesRepository.findById(dto.quizId())
                    .orElseThrow(() -> new RuntimeException("Quiz not found"));
            q.setQuiz(quiz);
        }
        q.setContent(dto.content());
        if (dto.type() != null) q.setType(dto.type());
        if (dto.orderIndex() != null) q.setOrderIndex(dto.orderIndex());
        return toDTO(questionsRepository.save(q));
    }

    @Override
    public void delete(Long id) {
        Questions q = questionsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found"));
        q.setDeleted(true);
        questionsRepository.save(q);
    }

    @Override
    public List<Questions> findRandom10ByQuizId(Long quizId) {
        return questionsRepository.findRandom10ByQuizId(quizId);
    }

    @Override
    @Transactional
    public void saveQuestion(QuestionUploadDto dto) {
        Quizzes quiz = quizzesRepository.findById(dto.quiz_id())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Quiz với ID: " + dto.quiz_id()));

        // 2. Tạo đối tượng Questions
        Questions question = new Questions();
        question.setQuiz(quiz);
        question.setContent(dto.content());
        question.setType(Questions.QuestionType.valueOf(dto.type()));

        // Tính order_index (đếm số câu của quiz này)
        int currentCount = questionsRepository.countByQuiz_Id(dto.quiz_id());
        question.setOrderIndex(currentCount + 1);

        // 3. Map list AnswerDto sang list Entity Answers và thiết lập quan hệ 2 chiều
        List<Answers> answerEntities = dto.answers().stream().map(aDto -> {
            Answers answer = new Answers();
            answer.setContent(aDto.content());
            answer.setCorrect(aDto.is_correct());
            answer.setQuestion(question);
            return answer;
        }).collect(Collectors.toList());

        question.setAnswers(answerEntities);

        questionsRepository.save(question);

    }

    @Transactional
    @Override
    public void importAll(List<QuestionUploadDto> dtoList) {
        if (dtoList == null || dtoList.isEmpty()) return;

        // 1. Lấy Quiz ID từ phần tử đầu tiên (vì tất cả thuộc cùng 1 Quiz)
        Long quizId = dtoList.get(0).quiz_id();
        Quizzes quiz = quizzesRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Quiz ID: " + quizId));

        // 2. Lấy order_index lớn nhất hiện tại trong DB của Quiz này
        int currentMaxOrder = questionsRepository.findMaxOrderIndexByQuizId(quizId);

        // 3. Duyệt danh sách DTO để chuyển thành Entity
        for (QuestionUploadDto dto : dtoList) {
            Questions question = new Questions();
            question.setContent(dto.content());
            question.setType(Questions.QuestionType.valueOf(dto.type()));
            question.setQuiz(quiz);
            currentMaxOrder++;
            question.setOrderIndex(currentMaxOrder);

            // 4. Map Answers và thiết lập quan hệ ngược (Bắt buộc để Cascade chạy)
            List<Answers> answers = dto.answers().stream().map(aDto -> {
                Answers answer = new Answers();
                answer.setContent(aDto.content());
                answer.setCorrect(aDto.is_correct());
                answer.setQuestion(question);
                return answer;
            }).collect(Collectors.toList());

            // 5. Gán list answers vào Question
            question.setAnswers(answers);

            // 6. Lưu Question (Hibernate tự lưu Answers nhờ CascadeType.ALL)
            questionsRepository.save(question);
        }
    }

    @Override
    public Page<Questions> getAllQuestionExam(Pageable pageable, String content, Long quizId) {
        if (content == null || content.trim().isEmpty()) {
            return questionsRepository.findAllByQuizId(quizId, pageable);
        }
        return questionsRepository.findAllByQuizIdAndContentContaining(quizId, content, pageable);
    }

    @Override
    public Questions getQuestionById(Long id) {
        return questionsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found"));
    }

    @Override
    public void saveFull(Long id, Questions dto) {
        questionsRepository.save(dto);
    }
}
