package com.example.ss6_quiz.repository;

import com.example.ss6_quiz.entity.Subjects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ISubjectsRepository extends JpaRepository<Subjects, Long> {
        Optional<Subjects> findByName(String name);

}
