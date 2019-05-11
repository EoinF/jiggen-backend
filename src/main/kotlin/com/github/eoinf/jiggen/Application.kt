package com.github.eoinf.jiggen

import com.badlogic.gdx.utils.GdxNativesLoader
import com.github.eoinf.jiggen.config.JiggenConfig
import com.github.eoinf.jiggen.endpoint.setupJsonResponse
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Component
import spark.Request
import spark.Spark.before
import spark.Spark.exception
import spark.Spark.halt
import spark.Spark.options
import spark.servlet.SparkApplication
import java.util.Base64

/**
 * Class required for deploying as a servlet
 */
@Component("application")
open class Application(
        private val jsonTransformer: JsonTransformer,
        private val jiggenConfig: JiggenConfig
        )
    : SparkApplication {

    private val logger = LogManager.getLogger()

    override fun init() {
        logger.info("Application::init: Initializing Application")

        // Required for using gdx tools (e.g. Pixmaps)
        GdxNativesLoader.load()

        exception(Exception::class.java) { e, req, res ->
            e.printStackTrace()

            res.body(
                    res.setupJsonResponse(
                            mapOf("error" to e.message), jsonTransformer
                    )
            )
            res.status(500)
        }

        options("/*"
        ) { request, response ->

            val accessControlRequestHeaders = request
                    .headers("Access-Control-Request-Headers")
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers",
                        accessControlRequestHeaders)
            }

            val accessControlRequestMethod = request
                    .headers("Access-Control-Request-Method")
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods",
                        accessControlRequestMethod)
            }

            "OK"
        }

        before("/*") {
            request, response ->
                if (request.requestMethod() != "GET" && !isAuthorized(request)) {
                    halt(401, "")
                }
                response.header("Access-Control-Allow-Origin", jiggenConfig.allowedOrigin)
                response.header("Access-Control-Expose-Headers", "Content-Length")
        }
    }

    private fun isAuthorized(request: Request): Boolean {
        val authHeader: String? = request.headers("Authorization")
        try {
            val token = authHeader!!.split(' ')[1]

            val userPass = String(
                    Base64.getDecoder().decode(token)
            ).split(':')

            return userPass[0] == jiggenConfig.authUsername && userPass[1] == jiggenConfig.authPassword
        } catch(ex: Exception) {
            logger.error("Failed to authenticate with auth header: $authHeader" )
            logger.error("Unauthorized access to ${request.url()} with ${request.requestMethod()}")
            return false
        }
    }
}