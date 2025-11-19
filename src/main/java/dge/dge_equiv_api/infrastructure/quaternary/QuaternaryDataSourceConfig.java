package dge.dge_equiv_api.infrastructure.quaternary;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "dge.dge_equiv_api.infrastructure.quaternary.repository", entityManagerFactoryRef = "quaternaryEntityManagerFactory", transactionManagerRef = "quaternaryTransactionManager")
public class QuaternaryDataSourceConfig {

    @Bean(name = "quaternaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.quaternary")
    public DataSource quaternaryDataSource(@Value("${spring.datasource.tertiary.url}") String url,
                                         @Value("${spring.datasource.tertiary.username}") String username,
                                         @Value("${spring.datasource.tertiary.password}") String password) {

        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setDriverClassName("org.postgresql.Driver");
        return ds;
    }

    @Bean(name = "quaternaryEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean quaternaryEntityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                               @Qualifier("tertiaryDataSource") DataSource dataSource) {

        Map<String, Object> props = new HashMap<>();
        props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        props.put("hibernate.hbm2ddl.auto", "none");
        props.put("hibernate.default_schema", "public");

        return builder.dataSource(dataSource).packages("dge.dge_equiv_api.infrastructure.quaternary")
                .persistenceUnit("quaternary").properties(props).build();
    }

    @Bean(name = "quaternaryTransactionManager")
    public PlatformTransactionManager quaternaryTransactionManager(
            @Qualifier("quaternaryEntityManagerFactory") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);

    }
}
