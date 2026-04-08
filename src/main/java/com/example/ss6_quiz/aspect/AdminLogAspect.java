package com.example.ss6_quiz.aspect;

import com.example.ss6_quiz.annotation.AdminActionLog;
import com.example.ss6_quiz.entity.AdminLogs;
import com.example.ss6_quiz.repository.IAdminLogsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;



import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class AdminLogAspect {

    @Autowired
    private IAdminLogsRepository adminLogRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Around("@annotation(adminLogAnnotation)")
    public Object logAdminAction(ProceedingJoinPoint joinPoint, AdminActionLog adminLogAnnotation) throws Throwable {
        String actionType = adminLogAnnotation.action();
        Object[] args = joinPoint.getArgs();
        String adminUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        Object result;
        try {
            result = joinPoint.proceed();

            saveLog(adminUsername, actionType, args, "SUCCESS");

            return result;
        } catch (Exception e) {

            saveLog(adminUsername, actionType, args, "FAILED: " + e.getMessage());
            throw e;
        }
    }

    private void saveLog(String user, String type, Object[] args, String status) {
        try {
            Map<String, Object> details = new HashMap<>();
            details.put("params", args);
            details.put("result", status);

            String jsonDetail = objectMapper.writeValueAsString(details);

            AdminLogs log = new AdminLogs();
            log.setAdminUsername(user);
            log.setActionType(type);
            log.setActionDetail(jsonDetail);
            log.setCreatedAt(LocalDateTime.now());
            adminLogRepository.save(log);
        } catch (Exception ex) {
            System.err.println("Lỗi khi ghi Audit Log: " + ex.getMessage());
        }
    }
}
