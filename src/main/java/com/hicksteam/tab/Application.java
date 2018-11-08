package com.hicksteam.tab;

import com.hicksteam.tab.db.gen.tables.records.TabUserRecord;
import com.hicksteam.tab.db.gen.tables.records.UserRoleRecord;
import org.apache.catalina.Context;
import org.apache.tomcat.util.scan.StandardJarScanner;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;

import static com.hicksteam.tab.db.gen.Tables.TAB_USER;
import static com.hicksteam.tab.db.gen.Tables.USER_ROLE;

@SpringBootApplication
public class Application
{
    private static final Logger log = LoggerFactory.getLogger(Application.class);
    private final DSLContext create;

    @Autowired
    public Application(DSLContext dslContext)
    {
        this.create = dslContext;
    }

    public static void main(String[] args)
    {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public TomcatServletWebServerFactory tomcatFactory()
    {
        return new TomcatServletWebServerFactory()
        {
            @Override
            protected void postProcessContext(Context context)
            {
                ((StandardJarScanner) context.getJarScanner()).setScanManifest(false);
            }
        };
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        PropertySourcesPlaceholderConfigurer props = new PropertySourcesPlaceholderConfigurer();
        props.setLocations(
                new ClassPathResource("git.properties"));
        return props;
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx)
    {
        return args -> {
            System.getProperties().setProperty("org.jooq.no-logo", "true");
            createUsersAndRoles();
        };
    }

    private void createUsersAndRoles()
    {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        Arrays.asList("eric", "steve").forEach(userName -> {
            // Create a new record
            TabUserRecord user = create.selectFrom(TAB_USER).where(TAB_USER.USERNAME.eq(userName)).fetchAny();
            if (user == null)
            {
                user = create.newRecord(TAB_USER);
                user.setUsername(userName);
                user.setPassword(encoder.encode(userName));
                user.setEnabled(true);
                user.store();
            }

            UserRoleRecord userRole = create.selectFrom(USER_ROLE).where(USER_ROLE.USERNAME.eq(userName)).and(USER_ROLE.ROLE.eq("user")).fetchAny();
            if (userRole == null)
            {
                userRole = create.newRecord(USER_ROLE);
                userRole.setUsername(userName);
                userRole.setRole("user");
                userRole.store();
            }
        });
    }
}