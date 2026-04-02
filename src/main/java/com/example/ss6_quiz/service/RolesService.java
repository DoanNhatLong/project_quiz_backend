package com.example.ss6_quiz.service;

import com.example.ss6_quiz.entity.Roles;
import com.example.ss6_quiz.repository.IRolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RolesService implements IRolesService {
    @Autowired
    IRolesRepository rolesRepository;

    @Override
    public Roles findById(Long id) {
        return rolesRepository.findById(id).orElse(null);
    }
}
