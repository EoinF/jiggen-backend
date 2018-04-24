package com.github.eoinf.jiggen;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import spark.servlet.SparkApplication;
import spark.servlet.SparkFilter;


@SpringBootApplication
@PropertySource(value = "file:/etc/config/jiggen/application.properties", ignoreResourceNotFound = true)
public class SpringifiedSparkFilter extends SparkFilter {

    private Logger logger = LogManager.getLogger();

    @Override
    protected SparkApplication getApplication(String applicationClassName) {
        try {
            ApplicationContext context = SpringApplication.run(SpringifiedSparkFilter.class);
            return (SparkApplication) context.getBean(applicationClassName);
        } catch (Exception exception) {
            logger.info("error loading spark filter", exception);
            throw exception;
        }
    }
}
