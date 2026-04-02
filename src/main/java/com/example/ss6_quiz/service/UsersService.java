package com.example.ss6_quiz.service;

import com.example.ss6_quiz.dto.UserSystemDto;
import com.example.ss6_quiz.entity.Roles;
import com.example.ss6_quiz.entity.Users;
import com.example.ss6_quiz.repository.IRolesRepository;
import com.example.ss6_quiz.repository.IUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UsersService implements IUsersService {
    @Autowired
    private IUsersRepository usersRepository;

    @Autowired
    private IRolesRepository rolesRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    @Override
    public Users getUserById(Long id) {
        return usersRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
    }

    @Override
    public Users createUser(Users user) {
        return usersRepository.save(user);
    }

    @Override
    public Users updateUser(Long id, Users userDetails) {
        Users user = getUserById(id);
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        user.setPassword(userDetails.getPassword());
        user.setXp(userDetails.getXp());
        user.setStreak(userDetails.getStreak());
        return usersRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        Users user = getUserById(id);
        user.setIsDeleted(true);
        usersRepository.save(user);
    }

    @Override
    public void registerUser(Users user) {
        if (usersRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (usersRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setIsDeleted(false);
        user.setStreak(0);
        user.setXp(100);
        user.setPoint(0);
        user.setCreatedAt(LocalDateTime.now());
        Roles userRole = rolesRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        user.setRoles(userRole);
        usersRepository.save(user);
    }

    @Override
    public Users login(String username, String password) {
        Users user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Tên đăng nhập không tồn tại"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Mật khẩu không chính xác");
        }

        return user;
    }

    @Override
    public Users findById(Long id) {
        return usersRepository.findById(id).orElse(null);
    }

    @Override
    public List<UserSystemDto> findAllUserSystemDto() {
        return usersRepository.findAllUserSystemDto();
    }

    @Override
    public Page<Users> findAllUsers(Pageable pageable, String username) {
        return usersRepository.findAllUsersByRoleUser(username, pageable);
    }

    @Override
    public void addXp(Long quizAttemptsId, Integer xp) {
        Users user = usersRepository.findByQuizAttemptsId(quizAttemptsId);
        user.setXp(user.getXp() + xp);
        usersRepository.save(user);
    }
}
