package com.example.ss6_quiz.controller;

import com.example.ss6_quiz.dto.CandidateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class WebSocketController {

    @MessageMapping("/hello")
    @SendTo("/topic/test")
    public String test(String message) {
        System.out.println("👉 BE nhận: " + message);
        return "Server nhận: " + message;
    }

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // Sửa phần khởi tạo Map ở phía trên cùng
    private Map<String, List<CandidateDto>> roomParticipants = new ConcurrentHashMap<>();

    @MessageMapping("/join_exam_room")
    public void handleJoinRoom(CandidateDto candidate) {
        String roomId = String.valueOf(candidate.challengeId()); // Ép về String cho chắc chắn

        // Thay ArrayList bằng java.util.concurrent.CopyOnWriteArrayList
        List<CandidateDto> participants = roomParticipants.computeIfAbsent(roomId,
                k -> new java.util.concurrent.CopyOnWriteArrayList<>());

        boolean isExist = participants.stream()
                .anyMatch(c -> String.valueOf(c.userId()).equals(String.valueOf(candidate.userId())));

        if (!isExist) {
            participants.add(candidate);
        }

        messagingTemplate.convertAndSend("/topic/update_candidates/" + roomId, participants);
    }

    @MessageMapping("/kick_candidate")
    public void handleKickCandidate(Map<String, Object> payload) {
        // 1. Lấy challengeId (thường là String hoặc Integer từ FE)
        String challengeId = String.valueOf(payload.get("challengeId"));

        // 2. Lấy userId an toàn (Xử lý trường hợp FE gửi số hoặc chuỗi)
        Object userIdObj = payload.get("userId");
        if (userIdObj == null) return;

        Long userIdToKick = Long.valueOf(userIdObj.toString());

        List<CandidateDto> participants = roomParticipants.get(challengeId);

        if (participants != null) {
            // 3. So sánh kiểu Long với Long (Giả định CandidateDto.userId() trả về Long/Integer)
            boolean removed = participants.removeIf(cand ->
                    Long.valueOf(cand.userId().toString()).equals(userIdToKick)
            );

            if (removed) {
                System.out.println("✅ Đã kick User " + userIdToKick + " khỏi phòng " + challengeId);

                // Gửi lại danh sách đã cập nhật
                messagingTemplate.convertAndSend("/topic/update_candidates/" + challengeId, participants);

                // Gửi tín hiệu kick riêng cho User đó - quan trọng là đường dẫn subscribe phải khớp
                messagingTemplate.convertAndSend("/topic/kicked/" + challengeId + "/" + userIdToKick, "KICKED_BY_ADMIN");
            }
        }
    }

}
