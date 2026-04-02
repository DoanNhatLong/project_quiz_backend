package com.example.ss6_quiz.service;

import java.util.Optional;

public interface IExamSnapshotService {
    void saveSnapshot(Long examId, Long userId, Object data);
    Optional<String> findRawDataByAttemptId(Long attemptId);
}
