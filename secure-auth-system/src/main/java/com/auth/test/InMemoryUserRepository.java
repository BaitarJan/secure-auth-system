package com.auth.test;

import com.auth.model.User;
import com.auth.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

// In-memory implementation for testing purposes only
public class InMemoryUserRepository implements UserRepository {

    private Map<String, User> users = new HashMap<>();
    private Integer currentId = 1;

    @Override
    public void save(User user) {
        user.setId(currentId++);
        users.put(user.getUsername(), user);
    }

    @Override
    public User findByUsername(String username) {
        return users.get(username);
    }

    @Override
    public void update(User user) {
        users.put(user.getUsername(), user);
    }
}
