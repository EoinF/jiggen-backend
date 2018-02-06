package com.github.eoinf.jiggen

import com.github.eoinf.jiggen.dao.IBackgroundDao
import com.github.eoinf.jiggen.dao.IImageDao
import com.github.eoinf.jiggen.dao.IPuzzleDao
import com.github.eoinf.jiggen.dao.ITemplateDao
import com.github.eoinf.jiggen.endpoint.*
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.task.AsyncListenableTaskExecutor
import spark.Spark.get
import spark.Spark.exception
import spark.servlet.SparkApplication
import java.util.concurrent.ExecutorService


/**
 * Class required for deploying as a servlet
 */
class Application(
        private val puzzleDao: IPuzzleDao,
        private val templateDao: ITemplateDao,
        private val backgroundDao: IBackgroundDao,
        private val imageDao: IImageDao,
        private val jsonTransformer: JsonTransformer)
    : SparkApplication {

    private val logger = LogManager.getLogger()

    @Value("jiggen.baseUrl")
    lateinit var baseUrl: String

    override fun init() {
        logger.info("Application::init: Initializing Application")

        exception(Exception::class.java) { e, req, res ->
            e.printStackTrace()
            res.setJsonContentType()
            res.body(
                    jsonTransformer.toJson(
                            mapOf("error" to e.message)
                    )
            )
        }

        get("/") { _, _ -> "This resource manages templates and cached puzzles for the jiggen game" }

        puzzlesEndpoint(puzzleDao, jsonTransformer, baseUrl)
        templatesEndpoint(templateDao, jsonTransformer, baseUrl)
        backgroundsEndpoint(backgroundDao, jsonTransformer, baseUrl)
        imagesEndpoint(imageDao, jsonTransformer)
    }
}
