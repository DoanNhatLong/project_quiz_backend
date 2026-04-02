package com.example.ss6_quiz.repository;

import com.example.ss6_quiz.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRolesRepository extends JpaRepository<Roles, Long> {
}
