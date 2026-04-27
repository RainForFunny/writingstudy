package com.ielts.writing.service;

import com.ielts.writing.model.entity.User;

public interface UserService {
    User register(String username, String password, String email);
    String login(String username, String password);
    User getCurrentUser(Long userId);
}