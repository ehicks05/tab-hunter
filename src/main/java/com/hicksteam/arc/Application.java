package com.hicksteam.arc;

import com.hicksteam.tab.db.gen.tables.UserRoles;
import com.hicksteam.tab.db.gen.tables.Users;
import com.hicksteam.tab.db.gen.tables.records.UserRolesRecord;
import com.hicksteam.tab.db.gen.tables.records.UsersRecord;
import org.jooq.DSLContext;
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

import java.util.Arrays;

import static com.hicksteam.tab.db.gen.Tables.USERS;
import static com.hicksteam.tab.db.gen.Tables.USER_ROLES;

@SpringBootApplication
public class Application
{
    private static final Logger log = LoggerFactory.getLogger(Application.class);
    private final JdbcTemplate jdbcTemplate;
    private final DSLContext create;

    @Autowired
    public Application(JdbcTemplate jdbcTemplate, DSLContext dslContext)
    {
        this.create = dslContext;
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
            createUsersAndRoles();
        };
    }

    private void createUsersAndRoles()
    {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        Arrays.asList("eric", "steve").forEach(userName -> {
            // Create a new record
            UsersRecord user = create.selectFrom(USERS).where(USERS.USERNAME.eq(userName)).fetchAny();
            if (user == null)
            {
                user = create.newRecord(Users.USERS);
                user.setUsername(userName);
                user.setPassword(encoder.encode(userName));
                user.setEnabled(true);
                user.store();
            }

            UserRolesRecord userRole = create.selectFrom(USER_ROLES).where(USER_ROLES.USERNAME.eq(userName)).and(USER_ROLES.ROLE.eq("user")).fetchAny();
            if (userRole == null)
            {
                userRole = create.newRecord(UserRoles.USER_ROLES);
                userRole.setUsername(userName);
                userRole.setRole("user");
                userRole.store();
            }
        });
    }
}