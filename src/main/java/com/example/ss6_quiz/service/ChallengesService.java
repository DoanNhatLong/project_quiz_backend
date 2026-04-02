package com.example.ss6_quiz.service;

import com.example.ss6_quiz.dto.ChallengesRequestDto;
import com.example.ss6_quiz.entity.ChallengeParticipant;
import com.example.ss6_quiz.entity.Challenges;
import com.example.ss6_quiz.entity.Exams;
import com.example.ss6_quiz.entity.Users;
import com.example.ss6_quiz.projection.ChallengesDetailProjection;
import com.example.ss6_quiz.repository.IChallengeParticipantRepository;
import com.example.ss6_quiz.repository.IChallengeRepository;
import com.example.ss6_quiz.repository.IExamsRepository;
import com.example.ss6_quiz.repository.IUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ChallengesService implements IChallengesService {
    @Autowired
    private IChallengeRepository challengeRepository;
    @Autowired
    private IChallengeParticipantRepository participantRepository;
    @Autowired
    private IUsersRepository userRepository;
    @Autowired
    private IExamsRepository examRepository;

    @Override
    @Transactional
    public Challenges createChallenger(ChallengesRequestDto dto) {
        Exams exam = examRepository.findById(dto.examId())
                .orElseThrow(() -> new RuntimeException("Đề thi không tồn tại với ID: " + dto.examId()));

        Users user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại với ID: " + dto.userId()));

        Challenges challenge = new Challenges();
        challenge.setTitle(dto.title());
        challenge.setAccessCode(dto.accessCode());
        challenge.setDurationMinutes(dto.durationMinutes());
        challenge.setAllowRejoin(dto.allowRejoin());

        challenge.setStartTime(LocalDateTime.parse(dto.startTime()));

        challenge.setExam(exam);
        challenge.setUser(user);

        challenge.setStatus(Challenges.ChallengeStatus.WAITING);
        challenge.setIsDeleted(false);
        challenge.setCreatedAt(LocalDateTime.now());

        return challengeRepository.save(challenge);
    }

    @Transactional
    @Override
    public String joinChallenger(Long challengeId, Long userId, String inputCode) {
        Challenges challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new RuntimeException("Thử thách không tồn tại!"));

        if (!challenge.getAccessCode().equals(inputCode)) {
            return "Mã tham gia không chính xác.";
        }

        if (challenge.getStatus() != Challenges.ChallengeStatus.WAITING) {
            return "Thử thách này đã bắt đầu hoặc đã kết thúc.";
        }

        boolean isAlreadyJoined = participantRepository.existsByChallengeIdAndUserId(challengeId, userId);
        if (isAlreadyJoined) {
            return "Bạn đã tham gia thử thách này rồi.";
        }

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại!"));

        ChallengeParticipant participant = new ChallengeParticipant();
        participant.setChallenge(challenge);
        participant.setUser(user);
        participant.setIsOnline(true);
        participant.setIsFocus(true);
        participant.setViolationCount(0);
        participant.setIsDisqualified(false);
        participant.setSubmitted(false);

        participantRepository.save(participant);

        return "SUCCESS";
    }

    @Override
    public List<Challenges> getWaitingChallengers() {
        return challengeRepository.findAllByStatus(Challenges.ChallengeStatus.WAITING);
    }

    @Override
    public List<Challenges> getAllChallenges() {
        return challengeRepository.findAll();
    }

    @Override
    public ChallengesDetailProjection getChallengeByIdUser(Long challengeId) {
        return challengeRepository.getChallengeById(challengeId).orElse(null);
    }

    @Override
    public Challenges getChallengeById(Long challengeId) {
        return challengeRepository.findById(challengeId).orElse(null);
    }

    @Override
    public List<ChallengesDetailProjection> findChallengeDetail() {
        return challengeRepository.findChallengeDetail();
    }
}
