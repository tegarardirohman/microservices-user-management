package com.tegar.user_service.service.impl;

import com.tegar.user_service.model.entity.User;
import com.tegar.user_service.repository.UserRedisRepository;
import com.tegar.user_service.repository.UserRepository;
import com.tegar.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserRedisRepository userRedisRepository;

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(userRepository.findAll());
    }

    @Override
    public Optional<User> getUserById(String id) {
        // Try to get from Redis first
        Optional<User> cachedUser = userRedisRepository.findById(id);
        if (cachedUser.isPresent()) {
            return cachedUser;
        }

        // If not in Redis, get from database and cache it
        Optional<User> user = userRepository.findById(id);
        user.ifPresent(userRedisRepository::save);
        return user;
    }

    @Override
    public User createUser(User user) {
        User savedUser = userRepository.save(user);
        userRedisRepository.save(savedUser);
        return savedUser;
    }

    @Override
    public User updateUser(User user) {
        User updatedUser = userRepository.update(user);
        userRedisRepository.save(updatedUser);
        return updatedUser;
    }

    @Override
    public void deleteUser(String id) {
        userRepository.delete(id);
        userRedisRepository.deleteById(id);
    }

    @Override
    public List<User> searchUsers(String keyword) {
        return new ArrayList<>(userRepository.search(keyword));
    }
}

