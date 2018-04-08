package com.github.eoinf.jiggen

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.PropertySource
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


//@Configuration
@PropertySource("classpath:/application.properties")
@SpringBootApplication
@EntityScan("com.github.eoinf.jiggen.data")
open class TestApplication {
    @Bean
    open fun executorService(): ExecutorService {
        return Executors.newSingleThreadExecutor()
    }
}

/*
    Entry point for running the app locally for dev testing
 */
fun main(args: Array<String>) {
    val context = SpringApplication.run(TestApplication::class.java)
    context.getBean(Application::class.java).init()
}