package com.example.ss6_quiz.service;

import com.example.ss6_quiz.entity.ExamSnapshot;
import com.example.ss6_quiz.repository.IExamSnapshotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ExamSnapshotService implements IExamSnapshotService {
    @Autowired
     private IExamSnapshotRepository examSnapshotRepository;

    @Override
    public void saveSnapshot(Long examId, Long userId, Object data) {
        ObjectMapper mapper = new ObjectMapper();
        ExamSnapshot snapshot = new ExamSnapshot();
        if (examSnapshotRepository.existsByExamIdAndUserId(examId, userId)) {
            return;
        }
        snapshot.setExamId(examId);
        snapshot.setUserId(userId);
        snapshot.setCreatedAt(LocalDateTime.now());
        snapshot.setSnapshotData(mapper.writeValueAsString(data));

        examSnapshotRepository.save(snapshot);

    }

    @Override
    public Optional<String> findRawDataByAttemptId(Long attemptId) {
        return examSnapshotRepository.findRawDataByAttemptId(attemptId);
    }
}
