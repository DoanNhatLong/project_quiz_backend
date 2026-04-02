package com.example.ss6_quiz.service;

import com.example.ss6_quiz.entity.Subjects;
import com.example.ss6_quiz.repository.ISubjectsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectsService implements ISubjectsService {
    @Autowired
    private ISubjectsRepository subjectsRepository;

    @Override
    public Subjects findByName(String name) {
        return subjectsRepository.findByName(name).orElse(null);
    }

    @Override
    public List<Subjects> findAll() {
        return subjectsRepository.findAll();
    }

    @Override
    public void create(String name) {
        Subjects subjects = new Subjects();
        subjects.setName(name);
        subjectsRepository.save(subjects);
    }
}
