package com.github.eoinf.jiggen.endpoint

import com.github.eoinf.jiggen.JsonTransformer
import com.github.eoinf.jiggen.dao.IPlayablePuzzleDao
import com.github.eoinf.jiggen.data.PlayablePuzzle
import com.github.eoinf.jiggen.data.PlayablePuzzleDTO
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Controller
import spark.Spark
import java.util.*

private val logger = LogManager.getLogger()
private const val playablePuzzles = PlayablePuzzle.RESOURCE_NAME

@Controller
class PlayablePuzzleController(playablePuzzleDao: IPlayablePuzzleDao, jsonTransformer: JsonTransformer) {
    init {
        Spark.path("/$playablePuzzles") {
            Spark.get("") { req, res ->
                logger.info("GET All request handled")
                res.setJsonContentType()
                jsonTransformer.toJson(playablePuzzleDao.get(req))
            }

            Spark.get("/today") { req, res ->
                logger.info("GET All Today request handled")
                res.setJsonContentType()
                jsonTransformer.toJson(playablePuzzleDao.getToday(req))
            }

            Spark.get("/:id") { req, res ->
                logger.info("GET request handled {}", req.params(":id"))
                val id = UUID.fromString(req.params(":id"))

                val playablePuzzle = playablePuzzleDao.get(req, id)

                if (playablePuzzle == null) {
                    res.status(404)
                    "Playable puzzle not found"
                } else {
                    res.setJsonContentType()
                    jsonTransformer.toJson(playablePuzzle)
                }
            }

            Spark.post("") { req, res ->
                val playablePuzzle = jsonTransformer.fromJson(req.body(), PlayablePuzzleDTO::class.java)
                logger.debug("POST request handled {}", playablePuzzle)

                when {
                    playablePuzzle.backgroundId == null -> {
                        res.status(400)
                        "background is required!"
                    }
                    playablePuzzle.generatedTemplateId == null -> {
                        res.status(400)
                        "generatedTemplate is required!"
                    }
                    else -> {
                        val result = playablePuzzleDao.save(req, playablePuzzle.copy(id = UUID.randomUUID()))

                        res.status(201)
                        res.setJsonContentType()
                        jsonTransformer.toJson(result)
                    }
                }
            }
        }
    }
}