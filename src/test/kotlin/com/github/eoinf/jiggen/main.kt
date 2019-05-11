package com.github.eoinf.jiggen

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import spark.servlet.SparkApplication

@SpringBootApplication
@EntityScan("com.github.eoinf.jiggen.data")
open class TestApplication

/*
    Entry point for running the app locally for dev testing
 */
fun main() {
    val context = SpringApplication.run(TestApplication::class.java)
    (context.getBean("application") as SparkApplication).init()
}
