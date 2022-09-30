package com.example.event_subscriber.dao;

import com.example.event_subscriber.dto.EventDto;
import com.example.event_subscriber.model.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EventDao {
    private final JdbcTemplate jdbcTemplate;

    public Optional<EventDto> findById(Long id){
        String sql = "select * from event " +
                "where id = ?;";
        List<EventDto> event = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(EventDto.class), id);
        if (event.size() == 1) {
            return Optional.of(event.get(0));
        }
        return Optional.empty();
    }

    public void dropTable() {
        String sql = "DROP TABLE IF EXISTS event CASCADE";
        jdbcTemplate.update(sql);
    }

    public void createTable() {
        String sql = """
                CREATE TABLE event (
                id BIGSERIAL PRIMARY KEY NOT NULL,
                date_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                name VARCHAR(500) NOT NULL,
                description TEXT DEFAULT 'no description'
                );""";
        jdbcTemplate.update(sql);
    }

    public List<EventDto> getAllEvents(){
        String sql = "select * from event ";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(EventDto.class));
    }

    public Long create(Event event){
        String sql = "insert into event (start_date, name, description) " +
                "values (?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setTimestamp(1, Timestamp.valueOf(event.getDateTime()));
            ps.setString(2, event.getName());
            ps.setString(3, event.getDescription());
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public void delete(Long id){
        String sql = "delete from event where id = ?;";
        jdbcTemplate.update(sql, id);
    }

    public List<Event> findBySubscribeEmail(String email){
        String sql = "select e.* " +
                "from event e, " +
                "     subscribe s " +
                "where s.email = ? " +
                "and e.id = s.event_id;";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Event.class), email);
    }
}
