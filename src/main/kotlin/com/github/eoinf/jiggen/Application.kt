package com.github.eoinf.jiggen

import com.badlogic.gdx.utils.GdxNativesLoader
import com.github.eoinf.jiggen.config.JiggenConfig
import com.github.eoinf.jiggen.dao.AtlasDao
import com.github.eoinf.jiggen.dao.IBackgroundDao
import com.github.eoinf.jiggen.dao.IGeneratedTemplateDao
import com.github.eoinf.jiggen.dao.IImageDao
import com.github.eoinf.jiggen.dao.IPlayablePuzzleDao
import com.github.eoinf.jiggen.dao.ITemplateDao
import com.github.eoinf.jiggen.endpoint.atlasesEndpoint
import com.github.eoinf.jiggen.endpoint.backgroundsEndpoint
import com.github.eoinf.jiggen.endpoint.baseEndpoint
import com.github.eoinf.jiggen.endpoint.generatedTemplatesEndpoint
import com.github.eoinf.jiggen.endpoint.imagesEndpoint
import com.github.eoinf.jiggen.endpoint.playablePuzzlesEndpoint
import com.github.eoinf.jiggen.endpoint.setJsonContentType
import com.github.eoinf.jiggen.endpoint.templatesEndpoint
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Component
import spark.Spark.before
import spark.Spark.exception
import spark.Spark.options
import spark.servlet.SparkApplication

/**
 * Class required for deploying as a servlet
 */
@Component("application")
open class Application(
        private val puzzleDao: IPlayablePuzzleDao,
        private val templateDao: ITemplateDao,
        private val backgroundDao: IBackgroundDao,
        private val imageDao: IImageDao,
        private val puzzleTemplateDao: IGeneratedTemplateDao,
        private val jsonTransformer: JsonTransformer,
        private val resourceMapper: ResourceMapper,
        private val atlasDao: AtlasDao,
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
            res.setJsonContentType()
            res.body(
                    jsonTransformer.toJson(
                            mapOf("error" to e.message)
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

        before("/*") { _, response -> response.header("Access-Control-Allow-Origin", jiggenConfig.allowedOrigin) }

        baseEndpoint(resourceMapper, jsonTransformer)
        templatesEndpoint(templateDao, jsonTransformer, resourceMapper)
        backgroundsEndpoint(backgroundDao, jsonTransformer, resourceMapper)
        imagesEndpoint(imageDao, jsonTransformer, resourceMapper)
        generatedTemplatesEndpoint(puzzleTemplateDao, jsonTransformer)
        playablePuzzlesEndpoint(puzzleDao, jsonTransformer)
        atlasesEndpoint(atlasDao, jsonTransformer, resourceMapper)
    }
}