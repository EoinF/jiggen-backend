package com.github.eoinf.jiggen

import com.github.eoinf.jiggen.dao.IBackgroundDao
import com.github.eoinf.jiggen.dao.IPuzzleDao
import com.github.eoinf.jiggen.dao.ITemplateDao
import com.github.eoinf.jiggen.endpoint.backgroundsEndpoint
import com.github.eoinf.jiggen.endpoint.puzzlesEndpoint
import com.github.eoinf.jiggen.endpoint.templatesEndpoint
import org.apache.logging.log4j.LogManager
import spark.Spark.get
import spark.Spark.exception
import spark.servlet.SparkApplication
import spark.Spark.before



/**
 * Class required for deploying as a servlet
 */
class Application(
        private var puzzleDao: IPuzzleDao,
        private var templateDao: ITemplateDao,
        private var backgroundDao: IBackgroundDao,
        private var jsonTransformer: JsonTransformer)
    : SparkApplication {

    private val logger = LogManager.getLogger()

    override fun init() {
        logger.info("Application::init: Initializing Application")
        exception(Exception::class.java) { e, req, res -> e.printStackTrace() }

        get("/") { _, _ -> "This resource manages users and cached puzzles for the jiggen game" }

        puzzlesEndpoint(puzzleDao, jsonTransformer)
        templatesEndpoint(templateDao, jsonTransformer)
        backgroundsEndpoint(backgroundDao, jsonTransformer)
    }
}
