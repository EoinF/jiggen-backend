package com.github.eoinf.jiggen

import org.apache.logging.log4j.LogManager
import spark.Spark.exception
import spark.Spark.get
import spark.Spark.post
import spark.servlet.SparkApplication

/**
 * Class required for deploying as a servlet
 */
class Application(private var puzzleDao: PuzzleDao, private var jsonTransformer: JsonTransformer)
    : SparkApplication {

    private val logger = LogManager.getLogger()

    override fun init() {
        println("Application::init: Initializing Application")
        logger.info("Application::init: Initializing Application")
        exception(Exception::class.java) { e, req, res -> e.printStackTrace() }

        get("/") { req, res -> "This resource manages users and cached puzzles for the jiggen game" }

        post("/puzzles/") { req, res ->
            val puzzle = CachedPuzzle()
            puzzleDao.post(puzzle)
        }
        get("/puzzles/:id") { req, res ->
            val id = req.params(":id").toLongOrNull()

            var output: CachedPuzzle? = null
            if (id == null) {
                res.status(404)
            } else {
                output = puzzleDao.get(id)
            }
            jsonTransformer.toJson(output)
        }
    }
}
