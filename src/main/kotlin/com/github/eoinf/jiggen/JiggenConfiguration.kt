package com.github.eoinf.jiggen

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "jiggen")
open class JiggenConfiguration {

    class OsConfig{
        var imageFolder: String? = null
        var atlasFolder: String? = null
    }

    var windows: OsConfig? = null
    var unix: OsConfig? = null

    var baseUrl: String? = null

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
}