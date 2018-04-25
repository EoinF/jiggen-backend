package com.github.eoinf.jiggen.endpoint

import com.github.eoinf.jiggen.JsonTransformer
import com.github.eoinf.jiggen.dao.IPuzzleTemplateDao
import com.github.eoinf.jiggen.data.PuzzleTemplate
import org.apache.logging.log4j.LogManager
import spark.Spark.get
import spark.Spark.path
import java.util.*

private val logger = LogManager.getLogger()
private const val puzzleTemplates = PuzzleTemplate.RESOURCE_NAME

fun puzzleTemplatesEndpoint(puzzleTemplateDao: IPuzzleTemplateDao, jsonTransformer: JsonTransformer) {
    path("/$puzzleTemplates") {
        get("") { req, res ->
            logger.info("GET All request handled")
            res.setJsonContentType()
            jsonTransformer.toJson(puzzleTemplateDao.get())
        }
        get("/:id") { req, res ->
            logger.info("GET request handled {}", req.params(":id"))
            val id = UUID.fromString(req.params(":id"))

            val puzzleTemplate = puzzleTemplateDao.get(id)

            if (puzzleTemplate == null) {
                res.status(404)
                ""
            } else {
                res.setJsonContentType()
                jsonTransformer.toJson(puzzleTemplate)
            }
        }
    }
}