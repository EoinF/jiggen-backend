package com.github.eoinf.jiggen

import com.badlogic.gdx.utils.GdxNativesLoader
import com.github.eoinf.jiggen.dao.*
import com.github.eoinf.jiggen.endpoint.*
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Component
import spark.Spark.exception
import spark.servlet.SparkApplication

/**
 * Class required for deploying as a servlet
 */
@Component("application")
open class Application(
        private val puzzleDao: IPuzzleDao,
        private val templateDao: ITemplateDao,
        private val backgroundDao: IBackgroundDao,
        private val imageDao: IImageDao,
        private val puzzleTemplateDao: IPuzzleTemplateDao,
        private val jsonTransformer: JsonTransformer,
        private val resourceMapper: ResourceMapper,
        private val atlasDao: AtlasDao
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

        baseEndpoint(resourceMapper, jsonTransformer)
        templatesEndpoint(templateDao, jsonTransformer, resourceMapper)
        backgroundsEndpoint(backgroundDao, jsonTransformer, resourceMapper)
        imagesEndpoint(imageDao, jsonTransformer, resourceMapper)
        puzzleTemplatesEndpoint(puzzleTemplateDao, jsonTransformer)
        atlasesEndpoint(atlasDao, jsonTransformer, resourceMapper)
    }
}