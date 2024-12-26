package com.tegar.user_service.service;

import com.tegar.user_service.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    Optional<User> getUserById(String id);
    User createUser(User user);
    User updateUser(User user);
    void deleteUser(String id);
    List<User> searchUsers(String keyword);
}