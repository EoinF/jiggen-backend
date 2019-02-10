package com.github.eoinf.jiggen.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Configuration
@ConfigurationProperties(prefix = "jiggen")
open class JiggenConfig {

    class OsConfig{
        var imageFolder: String? = null
        var atlasFolder: String? = null
    }

    var windows: OsConfig? = null
    var unix: OsConfig? = null

    var allowedOrigin: String? = null

    var authUsername: String? = null
    var authPassword: String? = null

    val imageFolder: String
        get() {
            return if (System.getProperty("os.name").contains("Windows")) {
                System.getenv("APPDATA") + windows!!.imageFolder!!
            } else {
                unix!!.imageFolder!!
            }
        }

    val atlasFolder: String
        get() {
            return if (System.getProperty("os.name").contains("Windows")) {
                System.getenv("APPDATA") + windows!!.atlasFolder!!
            } else {
                unix!!.atlasFolder!!
            }
        }


    @Bean
    open fun executorService(): ExecutorService {
        return Executors.newSingleThreadExecutor()
    }
}