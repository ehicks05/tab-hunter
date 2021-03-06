package com.hicksteam.tab;

import org.apache.catalina.Context;
import org.apache.tomcat.util.scan.StandardJarScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Application
{
    private static final Logger log = LoggerFactory.getLogger(Application.class);

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
        };
    }
}