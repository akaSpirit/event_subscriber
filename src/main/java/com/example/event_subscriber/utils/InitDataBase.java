package com.example.event_subscriber.utils;

import com.example.event_subscriber.dao.EventDao;
import com.example.event_subscriber.dao.SubscribeDao;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@RequiredArgsConstructor
public class InitDataBase {
    private final JdbcTemplate jdbcTemplate;

    @Bean
    public String init(EventDao eventDao, SubscribeDao subDao) {
        eventDao.dropTable();
        subDao.dropTable();
        eventDao.createTable();
        subDao.createTable();

        jdbcTemplate.execute("INSERT INTO event(date_time, name, description) VALUES (current_timestamp + interval '5 day', 'event1', 'description1')," +
                " (current_timestamp + interval '10 day', 'event2', 'description2')," +
                " (current_timestamp + interval '15 day', 'event3', 'description3')," +
                " (current_timestamp + interval '20 day', 'event4', 'description4')," +
                " (current_timestamp + interval '25 day', 'event5', 'description5')");

        jdbcTemplate.execute("INSERT INTO subscribe(event_id, email, register_date_time) VALUES (1, 'qwe@qwe', current_timestamp)," +
                " (2, 'asd@asd', current_timestamp)," +
                " (3, 'zxc@zxc', current_timestamp)," +
                " (4, 'ewq@ewq', current_timestamp)," +
                " (5, 'dsa@dsa', current_timestamp)");
        return "OK";
    }

}
