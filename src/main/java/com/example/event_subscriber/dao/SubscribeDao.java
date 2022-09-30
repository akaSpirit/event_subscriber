package com.example.event_subscriber.dao;

import com.example.event_subscriber.model.Subscribe;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SubscribeDao {
    private final JdbcTemplate jdbcTemplate;

    public Optional<Subscribe> findById(Long id){
        return Optional.empty();
    }

    public void dropTable() {
        String sql = "DROP TABLE IF EXISTS subscribe CASCADE";
        jdbcTemplate.update(sql);
    }

    public void createTable() {
        String query = """
                CREATE TABLE subscribe (
                id BIGSERIAL PRIMARY KEY NOT NULL,
                event_id BIGINT NOT NULL REFERENCES event (id),
                email VARCHAR(100) NOT NULL,
                register_date_time TIMESTAMP WITHOUT TIME ZONE NOT NULL
                );""";
        jdbcTemplate.update(query);
    }

    public List<Subscribe> findEventsByEmail(String email) {
        String query = "SELECT * FROM subscribe WHERE email = ?";
        return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Subscribe.class), email);
    }

    public Optional<Subscribe> findByEmailAndEventId(String email, Long eventId) {
        String sql = "select * \n" +
                "from subscribe s \n" +
                "where s.email = ? \n" +
                "and s.event_id = ?;";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Subscribe.class), email, eventId));
    }

    public Long create(String email, Long eventId) {
        String sql = "insert into subscribe (event_id, email, register_date_time) " +
                "values (?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, eventId);
            ps.setString(2, email);
            ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public String delete(String email, Long eventId){
        String sql = "DELETE FROM subscribe WHERE email = ? AND id = ?";
        jdbcTemplate.update(sql, email, eventId);
        return "Subscribe was deleted";
    }

}
