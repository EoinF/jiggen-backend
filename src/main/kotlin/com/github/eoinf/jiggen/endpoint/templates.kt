package com.github.eoinf.jiggen.endpoint

import com.github.eoinf.jiggen.JsonTransformer
import com.github.eoinf.jiggen.dao.ITemplateDao
import com.github.eoinf.jiggen.data.TemplateFile
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Value
import spark.Spark.*
import java.util.*

private val logger = LogManager.getLogger()

fun templatesEndpoint(templateDao: ITemplateDao, jsonTransformer: JsonTransformer, baseUrl: String) {
    path("/templates") {
        get("") { req, res ->
            logger.info("GET All request handled")
            setJsonContentType(res)
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
                setJsonContentType(res)
                jsonTransformer.toJson(template)
            }
        }
        post("") { req, res ->
            val template = jsonTransformer.fromJson(req.body(), TemplateFile::class.java)

            template.image = baseUrl + "/images/" + UUID.randomUUID()

            res.status(201)
            setJsonContentType(res)
            res.header("Location", template.image)
            jsonTransformer.toJson(template)
        }
    }

}