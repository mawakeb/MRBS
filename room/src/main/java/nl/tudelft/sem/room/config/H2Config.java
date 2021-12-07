package nl.tudelft.sem.room.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@EnableJpaRepositories("nl.tudelft.sem.room")
@PropertySource({"classpath:application.properties"})
@EnableTransactionManagement
public class H2Config {

    /**
     * Set up the connection to the database.
     */
    @Bean
    public DataSource dataSource(Environment environment) {
        String driverClassName = environment.getProperty("spring.datasource.driverClassName");

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Objects.requireNonNull(driverClassName));
        dataSource.setUrl(environment.getProperty("spring.datasource.url"));
        dataSource.setUsername(environment.getProperty("spring.datasource.username"));
        dataSource.setPassword(environment.getProperty("spring.datasource.password"));

        return dataSource;
    }
}

