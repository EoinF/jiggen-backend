package com.github.eoinf.jiggen;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import spark.servlet.SparkApplication;
import spark.servlet.SparkFilter;


@SpringBootApplication
public class SpringifiedSparkFilter extends SparkFilter {

    @Override
    protected SparkApplication getApplication(String applicationClassName) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        return (SparkApplication) context.getBean(applicationClassName);
    }
}
