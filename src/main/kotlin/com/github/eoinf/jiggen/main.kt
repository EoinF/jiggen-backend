package com.github.eoinf.jiggen

import spark.Spark.exception
import spark.Spark.get
import spark.servlet.SparkApplication

/**
 * Class required for deploying as a servlet
 */
class Application : SparkApplication {
    override fun init() {
        exception(Exception::class.java) { e, req, res -> e.printStackTrace() }

        get("/") { req, res -> "This resource manages users and cached puzzles for the jiggen game" }
        get("/hello") { req, res -> "Hello World" }
    }
}


fun main(args: Array<String>) {
    Application()
}