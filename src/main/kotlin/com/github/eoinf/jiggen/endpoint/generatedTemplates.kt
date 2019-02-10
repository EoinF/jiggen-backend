package com.github.eoinf.jiggen.endpoint

import com.github.eoinf.jiggen.JsonTransformer
import com.github.eoinf.jiggen.dao.IGeneratedTemplateDao
import com.github.eoinf.jiggen.data.GeneratedTemplate
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Controller
import spark.Spark.get
import spark.Spark.path
import java.util.*

private val logger = LogManager.getLogger()
private const val puzzleTemplates = GeneratedTemplate.RESOURCE_NAME

@Controller
class GeneratedTemplatesController(generatedTemplateDao: IGeneratedTemplateDao, jsonTransformer: JsonTransformer) {
    init {
        path("/$puzzleTemplates") {
            get("") { req, res ->
                logger.info("GET All request handled")
                res.setJsonContentType()
                jsonTransformer.toJson(generatedTemplateDao.get(req))
            }
            get("/:id") { req, res ->
                logger.info("GET request handled {}", req.params(":id"))
                val id = UUID.fromString(req.params(":id"))

                val generatedTemplate = generatedTemplateDao.get(req, id)

                if (generatedTemplate == null) {
                    res.status(404)
                    ""
                } else {
                    res.setJsonContentType()
                    jsonTransformer.toJson(generatedTemplate)
                }
            }
        }
    }
}