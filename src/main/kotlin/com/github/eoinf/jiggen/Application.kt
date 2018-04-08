package com.github.eoinf.jiggen

import com.badlogic.gdx.utils.GdxNativesLoader
import com.github.eoinf.jiggen.dao.*
import com.github.eoinf.jiggen.endpoint.*
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Component
import spark.Spark.exception
import spark.Spark.get
import spark.servlet.SparkApplication

/**
 * Class required for deploying as a servlet
 */
@Component
class Application(
        private val puzzleDao: IPuzzleDao,
        private val templateDao: ITemplateDao,
        private val backgroundDao: IBackgroundDao,
        private val imageDao: IImageDao,
        private val puzzleTemplateDao: IPuzzleTemplateDao,
        private val jsonTransformer: JsonTransformer,
        private val jiggenConfiguration: JiggenConfiguration
        )
    : SparkApplication {

    private val logger = LogManager.getLogger()

    override fun init() {
        logger.info("Application::init: Initializing Application")

        // Required for using gdx tools (e.g. Pixmaps)
        GdxNativesLoader.load()

        exception(Exception::class.java) { e, req, res ->
            e.printStackTrace()
            res.setJsonContentType()
            res.body(
                    jsonTransformer.toJson(
                            mapOf("error" to e.message)
                    )
            )
            res.status(500)
        }

        get("/") { _, _ -> "This resource manages templates and cached puzzles for the jiggen game" }

        templatesEndpoint(templateDao, puzzleTemplateDao, jsonTransformer, jiggenConfiguration.baseUrl!!)
        backgroundsEndpoint(backgroundDao, jsonTransformer, jiggenConfiguration.baseUrl!!)
        imagesEndpoint(imageDao, jsonTransformer)
        puzzleTemplatesEndpoint(puzzleTemplateDao, jsonTransformer)
    }
}
