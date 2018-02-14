package com.github.eoinf.jiggen.endpoint

import com.github.eoinf.jiggen.JsonTransformer
import com.github.eoinf.jiggen.dao.IPuzzleTemplateDao
import org.apache.logging.log4j.LogManager
import spark.Spark
import java.util.*

private val logger = LogManager.getLogger()

fun puzzleTemplatesEndpoint(puzzleTemplateDao: IPuzzleTemplateDao, jsonTransformer: JsonTransformer) {
    Spark.path("/templates/:id/puzzletemplates") {
        Spark.get("") { req, res ->
            logger.info("GET request handled {}", req.params(":id"))
            val templateId = UUID.fromString(req.params(":id"))
            res.setJsonContentType()
            jsonTransformer.toJson(puzzleTemplateDao.getByTemplateId(templateId))
        }
        Spark.get("/:ptid") { req, res ->
            logger.info("GET request handled {}", req.params(":id"))
            val templateId = UUID.fromString(req.params(":id"))
            val puzzleTemplateId = UUID.fromString(req.params(":ptid"))

            val puzzleTemplates = puzzleTemplateDao.getByTemplateId(templateId)
            val template = puzzleTemplates.stream()
                    .filter { it.id == puzzleTemplateId }
                    .findFirst()

            if (!template.isPresent) {
                res.status(404)
                ""
            } else {
                res.setJsonContentType()
                jsonTransformer.toJson(template.get())
            }
        }
    }
}