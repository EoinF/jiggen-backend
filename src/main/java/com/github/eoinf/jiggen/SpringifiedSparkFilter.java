package com.github.eoinf.jiggen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Repository;
import spark.servlet.SparkApplication;
import spark.servlet.SparkFilter;


@SpringBootApplication
@Repository("com.github.eoinf.jiggen")
@ImportResource({"classpath*:applicationContext.xml"})
public class SpringifiedSparkFilter extends SparkFilter {

    @Override
    protected SparkApplication getApplication(String applicationClassName) {
        ApplicationContext context = SpringApplication.run(SpringifiedSparkFilter.class);
        return (SparkApplication) context.getBean("Application");
    }
}
