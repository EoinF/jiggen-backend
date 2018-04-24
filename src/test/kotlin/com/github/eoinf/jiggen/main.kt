package com.github.eoinf.jiggen

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import spark.servlet.SparkApplication

//@PropertySource("classpath:/application.properties")
@SpringBootApplication
@EntityScan("com.github.eoinf.jiggen.data")
open class TestApplication

/*
    Entry point for running the app locally for dev testing
 */
fun main(args: Array<String>) {
    val context = SpringApplication.run(TestApplication::class.java)
    (context.getBean("application") as SparkApplication).init()
}