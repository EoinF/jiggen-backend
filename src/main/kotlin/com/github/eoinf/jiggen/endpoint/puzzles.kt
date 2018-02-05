package com.github.eoinf.jiggen.endpoint

import com.github.eoinf.jiggen.JsonTransformer
import com.github.eoinf.jiggen.dao.IPuzzleDao
import com.github.eoinf.jiggen.data.FinishedPuzzle
import org.apache.logging.log4j.LogManager
import spark.Spark.path
import spark.Spark.get
import spark.Spark.post

private val logger = LogManager.getLogger()

fun puzzlesEndpoint(puzzleDao: IPuzzleDao, jsonTransformer: JsonTransformer, baseUrl: String) {
    path("/puzzles") {
        get("") { req, res ->
            logger.info("GET All request handled")
            setJsonContentType(res)
            jsonTransformer.toJson(puzzleDao.get())
        }
        get("/:id") { req, res ->
            logger.info("GET request handled {}", req.params(":id"))
            val id = req.params(":id").toIntOrNull()

            val puzzle: FinishedPuzzle? = puzzleDao.get(id)

            if (puzzle == null) {
                res.status(404)
                ""
            } else {
                setJsonContentType(res)
                jsonTransformer.toJson(puzzle)
            }
        }
        post("") { req, res ->
            logger.info("POST request handled")
            var puzzle = jsonTransformer.fromJson(req.body(), FinishedPuzzle::class.java)

            if (puzzle.template != null && puzzle.background != null) {
                puzzle = puzzleDao.post(puzzle)
                res.status(201)
                setJsonContentType(res)
                jsonTransformer.toJson(puzzle)
            } else {
                res.status(400)
                "Missing required parameters: template, background"
            }
        }
    }

}