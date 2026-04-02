package com.example.ss6_quiz.service;

import com.example.ss6_quiz.entity.Subjects;

import java.util.List;

public interface ISubjectsService {
    Subjects findByName(String name);
    List<Subjects> findAll();
    void create(String name);
}
