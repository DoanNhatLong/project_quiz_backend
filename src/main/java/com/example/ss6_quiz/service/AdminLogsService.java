package com.example.ss6_quiz.service;

import com.example.ss6_quiz.entity.AdminLogs;
import com.example.ss6_quiz.projection.CountStatsProjection;
import com.example.ss6_quiz.repository.IAdminLogsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminLogsService implements IAdminLogsService {
    @Autowired
    IAdminLogsRepository adminLogsRepository;

    @Override
    public List<AdminLogs> getLogs(String actionType) {

        Sort sortDesc = Sort.by(Sort.Direction.DESC, "createdAt");
        if (actionType == null || actionType.trim().isEmpty()) {
            return adminLogsRepository.findAll(sortDesc);
        }
        return adminLogsRepository.findAllByActionTypeContaining(actionType, sortDesc);
    }

    @Override
    public CountStatsProjection getCountStats() {
        return adminLogsRepository.getCountStats();
    }

    @Override
    public void softDelete(Long id){
        adminLogsRepository.softDelete(id);
    }
}

