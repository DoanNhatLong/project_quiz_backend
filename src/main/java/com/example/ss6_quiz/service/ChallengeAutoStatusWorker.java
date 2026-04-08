package com.example.ss6_quiz.service;

import com.example.ss6_quiz.repository.IChallengeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class ChallengeAutoStatusWorker {

    @Autowired
    private IChallengeRepository challengesRepository;


    @Scheduled(fixedRate = 30000)
    @Transactional
    public void autoUpdateChallengeStatus() {
        System.out.println("CHeck");
        int startedCount = challengesRepository.startAllReachableChallenges();
        if (startedCount > 0) {
            log.info("Hệ thống tự động BẮT ĐẦU {} cuộc thi.", startedCount);
        }

        int endedCount = challengesRepository.endExpiredChallenges();
        if (endedCount > 0) {
            log.info("Hệ thống tự động KẾT THÚC {} cuộc thi.", endedCount);
        }
    }
}