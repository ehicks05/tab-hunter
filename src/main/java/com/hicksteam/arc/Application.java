package com.hicksteam.arc;

import com.hicksteam.arc.entities.User;
import com.hicksteam.arc.entities.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class Application
{
    private static final Logger log = LoggerFactory.getLogger(Application.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public Application(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
        DAO.setJdbcTemplate(jdbcTemplate);
    }

    public static void main(String[] args)
    {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx)
    {
        return args -> {
            createTables();
            createUsersAndRoles();
        };
    }

    private void createTables()
    {
        log.info("Creating tables");

        jdbcTemplate.execute("DROP TABLE IF EXISTS tabs CASCADE ");
        jdbcTemplate.execute("CREATE TABLE tabs(id SERIAL, title VARCHAR(2000), content VARCHAR(2000), author_id bigint)");
        log.info("created table TABS");

        jdbcTemplate.execute("DROP TABLE IF EXISTS comments CASCADE");
        jdbcTemplate.execute("CREATE TABLE comments(id SERIAL, post_id bigint, parent_comment_id bigint, author_id bigint, content VARCHAR(255), CONSTRAINT pk_comment PRIMARY KEY (id))");
        log.info("created table COMMENTS");

        jdbcTemplate.execute("DROP TABLE IF EXISTS users CASCADE");
        jdbcTemplate.execute("CREATE TABLE users(user_id SERIAL, username varchar(20) NOT NULL PRIMARY KEY, password varchar(200) NOT NULL, enabled boolean NOT NULL DEFAULT FALSE)");
        log.info("created table USERS");

        jdbcTemplate.execute("DROP TABLE IF EXISTS user_roles CASCADE");
        jdbcTemplate.execute("CREATE TABLE user_roles(user_role_id SERIAL PRIMARY KEY, username varchar(20) NOT NULL, role varchar(20) NOT NULL, UNIQUE (username, role), FOREIGN KEY (username) REFERENCES users (username) )");
        log.info("created table USER_ROLES");
    }

    private void createUsersAndRoles()
    {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        User eric = new User("eric", encoder.encode("eric"), true);
        eric.setUserId(User.createUser(eric));

        User steve = new User("steve", encoder.encode("steve"), true);
        steve.setUserId(User.createUser(steve));

        UserRole.createUserRole(new UserRole(eric.getUsername(), "user"));
        UserRole.createUserRole(new UserRole(steve.getUsername(), "user"));
    }
}