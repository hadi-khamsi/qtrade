package com.qtrade.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.qtrade.repository.broker",
        entityManagerFactoryRef = "brokerEntityManagerFactory",
        transactionManagerRef = "brokerTransactionManager"
)
public class BrokerDataSourceConfig {

    @Primary
    @Bean(name = "brokerDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.broker")
    public DataSource brokerDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "brokerEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean brokerEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("brokerDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.qtrade.entity.broker")
                .persistenceUnit("broker")
                .build();
    }

    @Primary
    @Bean(name = "brokerTransactionManager")
    public PlatformTransactionManager brokerTransactionManager(
            @Qualifier("brokerEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}