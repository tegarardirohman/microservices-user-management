package com.tegar.user_service.repository;


import com.tegar.user_service.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    private final String FIND_ALL_QUERY = "SELECT * FROM users";
    private final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
    private final String INSERT_QUERY = """
        INSERT INTO users (id, email, full_name, phone_number, address, 
        date_of_birth, avatar_url, role, created_at, updated_at)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
    """;
    private final String UPDATE_QUERY = """
        UPDATE users SET email = ?, full_name = ?, phone_number = ?, 
        address = ?, date_of_birth = ?, avatar_url = ?, role = ?, 
        updated_at = CURRENT_TIMESTAMP WHERE id = ?
    """;
    private final String DELETE_QUERY = "DELETE FROM users WHERE id = ?";
    private final String SEARCH_QUERY = """
        SELECT * FROM users WHERE 
        LOWER(full_name) LIKE LOWER(?) OR 
        LOWER(email) LIKE LOWER(?) OR 
        LOWER(phone_number) LIKE LOWER(?)
    """;

    private final RowMapper<User> userRowMapper = (rs, rowNum) ->
        User.builder()
            .id(rs.getString("id"))
            .email(rs.getString("email"))
            .fullName(rs.getString("full_name"))
            .phoneNumber(rs.getString("phone_number"))
            .address(rs.getString("address"))
            .dateOfBirth(rs.getDate("date_of_birth") != null ? rs.getDate("date_of_birth").toLocalDate() : null)
            .avatarUrl(rs.getString("avatar_url"))
            .role(rs.getString("role"))
            .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
            .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
            .build();

    public List<User> findAll() {
        return jdbcTemplate.query(FIND_ALL_QUERY, userRowMapper);
    }

    public Optional<User> findById(String id) {
        List<User> results = jdbcTemplate.query(FIND_BY_ID_QUERY, userRowMapper, id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public User save(User user) {
        jdbcTemplate.update(INSERT_QUERY,
                user.getId(), user.getEmail(), user.getFullName(),
                user.getPhoneNumber(), user.getAddress(), user.getDateOfBirth(),
                user.getAvatarUrl(), user.getRole()
        );
        return findById(user.getId()).orElseThrow();
    }

    public User update(User user) {
        jdbcTemplate.update(UPDATE_QUERY,
                user.getEmail(), user.getFullName(), user.getPhoneNumber(),
                user.getAddress(), user.getDateOfBirth(), user.getAvatarUrl(),
                user.getRole(), user.getId()
        );
        return findById(user.getId()).orElseThrow();
    }

    public void delete(String id) {
        jdbcTemplate.update(DELETE_QUERY, id);
    }

    public List<User> search(String keyword) {
        String searchTerm = "%" + keyword + "%";
        return jdbcTemplate.query(SEARCH_QUERY, userRowMapper,
                searchTerm, searchTerm, searchTerm);
    }
}
