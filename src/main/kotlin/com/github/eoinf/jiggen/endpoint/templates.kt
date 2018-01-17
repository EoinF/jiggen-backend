package com.github.eoinf.jiggen.endpoint

import com.github.eoinf.jiggen.JsonTransformer
import com.github.eoinf.jiggen.dao.ITemplateDao
import com.github.eoinf.jiggen.data.TemplateFile
import org.apache.logging.log4j.LogManager
import spark.Spark.path
import spark.Spark.get

private val logger = LogManager.getLogger()

fun templatesEndpoint(templateDao: ITemplateDao, jsonTransformer: JsonTransformer) {
    path("/templates") {
        get("") { req, res ->
            logger.info("GET All request handled")
            setJsonContent(res)
            jsonTransformer.toJson(templateDao.get())
        }
        get("/:id") { req, res ->
            logger.info("GET request handled {}", req.params(":id"))
            val id = req.params(":id").toIntOrNull()

            val template: TemplateFile? = templateDao.get(id)

            if (template == null) {
                res.status(404)
                ""
            } else {
                setJsonContent(res)
                jsonTransformer.toJson(template)
            }
        }
    }

}