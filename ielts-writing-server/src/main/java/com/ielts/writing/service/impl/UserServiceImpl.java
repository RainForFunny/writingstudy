package com.ielts.writing.service.impl;

import com.ielts.writing.exception.BusinessException;
import com.ielts.writing.model.entity.User;
import com.ielts.writing.repository.UserRepository;
import com.ielts.writing.service.UserService;
import com.ielts.writing.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public User register(String username, String password, String email) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new BusinessException("用户名已存在");
        }

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.insert(user);
        return user;
    }

    @Override
    public String login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("用户名或密码错误"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }

        return jwtUtil.generateToken(user.getId(), user.getUsername());
    }

    @Override
    public User getCurrentUser(Long userId) {
        return userRepository.selectById(userId);
    }
}