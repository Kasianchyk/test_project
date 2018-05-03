package ru.javabegin.training.spring;

import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.javabegin.training.spring.aop.postprocessors.TransactionAspect;


import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = "ru.javabegin.training.spring")
@EnableTransactionManagement
@EnableAspectJAutoProxy
public class Config {

    @Bean
    @Lazy
    public DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/test_db?verifyServerCertificate=false&useSSL=false&requireSSL=false");
        dataSource.setUsername("root");
        dataSource.setPassword("");
        return dataSource;
    }

    @Bean
    public DataSourceTransactionManager transactionManager(){
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource());
        return transactionManager;
    }

}
