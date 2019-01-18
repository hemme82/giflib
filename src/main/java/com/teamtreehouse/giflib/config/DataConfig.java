package com.teamtreehouse.giflib.config;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;


@Configuration
@PropertySource("app.properties")
public class DataConfig {
    //spring will load info from app.properties and store in env variable.
    @Autowired
    private Environment env;
    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        Resource config = new ClassPathResource("hibernate.cfg.xml");
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setConfigLocation(config);
        //evaluates to value equal to giflib.entity.package from properties.
        sessionFactory.setPackagesToScan(env.getProperty("giflib.entity.package"));
        sessionFactory.setDataSource(dataSource());
        return sessionFactory;
    }
    @Bean
    public DataSource dataSource() {
        BasicDataSource ds = new BasicDataSource();

        // Driver class name
        ds.setDriverClassName(env.getProperty("giflib.db.driver"));
        // set url
        ds.setUrl(env.getProperty("giflib.db.url"));
        // set username and password
        ds.setUsername(env.getProperty("giflib.db.username"));
        ds.setPassword(env.getProperty("giflib.db.password"));

        return ds;
    }
}
