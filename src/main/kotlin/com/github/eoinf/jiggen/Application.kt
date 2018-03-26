package com.github.eoinf.jiggen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.GdxNativesLoader
import com.github.eoinf.jiggen.PuzzleExtractor.Decoder.DecodedTemplate
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzleFactory
import com.github.eoinf.jiggen.dao.*
import com.github.eoinf.jiggen.endpoint.*
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Value
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
        private val puzzleTemplateDao: IPuzzleTemplateDao,
        private val jsonTransformer: JsonTransformer)
    : SparkApplication {

    private val logger = LogManager.getLogger()

    @Value("jiggen.baseUrl")
    lateinit var baseUrl: String

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

        templatesEndpoint(templateDao, jsonTransformer, baseUrl)
        backgroundsEndpoint(backgroundDao, jsonTransformer, baseUrl)
        imagesEndpoint(imageDao, jsonTransformer)
        puzzleTemplatesEndpoint(puzzleTemplateDao, jsonTransformer)
    }
}
