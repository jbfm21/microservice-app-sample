package br.com.example.microservice.order.infraestructure.bootstrap;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;


/**
 * Datasource master
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"br.com.example.microservice.order.infraestructure.repository"},entityManagerFactoryRef = "orderProjectionDatabaseEntityManager")
public class MasterDataSourceConfiguration {
    @Primary
    @Bean("orderProjectionDatabase")
    @ConfigurationProperties("spring.datasource.hikari.order-projection")
    public DataSource master() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Primary
    @Bean(name = "orderProjectionDatabaseEntityManager")
    public LocalContainerEntityManagerFactoryBean orderProjectionDatabaseEntityManager(EntityManagerFactoryBuilder builder) {
        return  builder.dataSource(master())
                .persistenceUnit("orderProjectionDatabase")
                .properties(jpaProperties())
                .packages("br.com.example.microservice.order.infraestructure.repository", "br.com.example.microservice.order.infraestructure.entity")
                .build();
    }

    private Map<String, Object> jpaProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put("hibernate.physical_naming_strategy", SpringPhysicalNamingStrategy.class.getName());
        props.put("hibernate.implicit_naming_strategy", SpringImplicitNamingStrategy.class.getName());
        props.put("hibernate.hbm2ddl.auto", "update");
        props.put("hibernate.show_sql", "true");
        return props;
    }
}
