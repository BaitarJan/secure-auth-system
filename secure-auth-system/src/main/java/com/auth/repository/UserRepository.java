package com.auth.repository;

import com.auth.model.User;

public interface UserRepository {
    void save(User user);
    User findByUsername(String username);
    void update(User user);
}
