package com.qtrade.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.qtrade.repository.exchange",
        entityManagerFactoryRef = "exchangeEntityManagerFactory",
        transactionManagerRef = "exchangeTransactionManager"
)
public class ExchangeDataSourceConfig {

    @Bean(name = "exchangeDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.exchange")
    public DataSource exchangeDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "exchangeEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean exchangeEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("exchangeDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.qtrade.entity.exchange")
                .persistenceUnit("exchange")
                .build();
    }

    @Bean(name = "exchangeTransactionManager")
    public PlatformTransactionManager exchangeTransactionManager(
            @Qualifier("exchangeEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}