package br.com.example.microservice.order.infraestructure.bootstrap;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.axonframework.common.jpa.EntityManagerProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
public class AxonEventStoreConfig {
    @Bean("axonMaster")
    @ConfigurationProperties("spring.datasource.hikari.axon-master")
    public DataSource axon() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean(name="entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder, @Qualifier("axonMaster") DataSource axonMaster) {
        return builder
                .dataSource(axonMaster)
                .persistenceUnit("axonMaster")
                .properties(jpaProperties())
                .packages(
                		//"br.com.example.microservice.order.infraestructure.repository", "br.com.example.microservice.order.infraestructure.entity",
                		
                		"org.axonframework.eventhandling.tokenstore",
                        "org.axonframework.modelling.saga.repository.jpa",
                        "org.axonframework.eventsourcing.eventstore.jpa")
                .build();
    }

    /**
     * For axon framework
     * @param entityManagerFactory
     * @return
     */
    @Bean
    public EntityManagerProvider entityManagerProvider(@Qualifier("entityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        return () -> entityManagerFactory.getObject().createEntityManager();
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
