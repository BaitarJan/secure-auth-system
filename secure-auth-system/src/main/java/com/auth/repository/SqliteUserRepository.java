package com.auth.repository;

import com.auth.model.User;

import java.sql.*;
import java.time.LocalDateTime;

public class SqliteUserRepository implements UserRepository {

    private Connection conn;

    public SqliteUserRepository(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void save(User user) {
        String sql = "INSERT INTO users(username, email, password_hash, failed_attempts) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPasswordHash());
            ps.setInt(4, user.getFailedAttempts());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password_hash")
                );

                user.setFailedAttempts(rs.getInt("failed_attempts"));
                user.setLockUntil(
                        rs.getString("lock_until") != null
                                ? LocalDateTime.parse(rs.getString("lock_until"))
                                : null
                );
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE users SET failed_attempts = ?, lock_until = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, user.getFailedAttempts());
            ps.setString(2, user.getLockUntil() != null ? user.getLockUntil().toString() : null);
            ps.setInt(3, user.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}