package com.tegar.auth_service.repository;

import com.tegar.auth_service.model.entity.User;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public UserRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = :email";
        var params = new MapSqlParameterSource().addValue("email", email);

        return jdbcTemplate.query(sql, params, (rs, rowNum) ->
                User.builder()
                        .id(UUID.fromString(rs.getString("id")))
                        .email(rs.getString("email"))
                        .password(rs.getString("password"))
                        .role(rs.getString("role"))
                        .build()
        ).stream().findFirst();
    }

    public void save(User user) {
        String sql = "INSERT INTO users (id, email, password, role) VALUES (:id, :email, :password, :role)";
        var params = new MapSqlParameterSource()
                .addValue("id", user.getId())
                .addValue("email", user.getEmail())
                .addValue("password", user.getPassword())
                .addValue("role", user.getRole());
        jdbcTemplate.update(sql, params);
    }
}
