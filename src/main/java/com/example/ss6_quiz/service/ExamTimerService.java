package com.example.ss6_quiz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ExamTimerService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // Lưu trữ các bộ đếm đang chạy: <attemptId, ScheduledFuture>
    private final Map<String, ScheduledFuture<?>> timers = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);

    public void startExamTimer(String attemptId, int durationMinutes) {
        // Nếu đã có timer cho attempt này rồi thì không tạo mới (tránh F5 tạo thêm task)
        if (timers.containsKey(attemptId)) return;

        AtomicInteger remainingSeconds = new AtomicInteger(durationMinutes * 60);

        ScheduledFuture<?> task = scheduler.scheduleAtFixedRate(() -> {
            int seconds = remainingSeconds.decrementAndGet();

            // Gửi giây còn lại về cho FE qua topic đã thống nhất
            messagingTemplate.convertAndSend("/topic/exam_timer/" + attemptId, seconds);

            if (seconds <= 0) {
                stopTimer(attemptId);
                // Bạn có thể gọi thêm logic chốt điểm ở DB tại đây nếu muốn bảo mật
            }
        }, 0, 1, TimeUnit.SECONDS);

        timers.put(attemptId, task);
    }

    public void stopTimer(String attemptId) {
        ScheduledFuture<?> task = timers.remove(attemptId);
        if (task != null) {
            task.cancel(false);
        }
    }
}
