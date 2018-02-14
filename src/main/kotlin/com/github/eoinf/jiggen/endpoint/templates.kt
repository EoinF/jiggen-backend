package com.github.eoinf.jiggen.endpoint

import com.github.eoinf.jiggen.JsonTransformer
import com.github.eoinf.jiggen.dao.ITemplateDao
import com.github.eoinf.jiggen.data.TemplateFile
import org.apache.logging.log4j.LogManager
import spark.Spark.*
import java.util.*

private val logger = LogManager.getLogger()

fun templatesEndpoint(templateDao: ITemplateDao, jsonTransformer: JsonTransformer, baseUrl: String) {
    path("/templates") {
        get("") { req, res ->
            logger.info("GET All request handled")
            res.setJsonContentType()
            jsonTransformer.toJson(templateDao.get())
        }
        get("/:id") { req, res ->
            logger.info("GET request handled {}", req.params(":id"))
            val id = UUID.fromString(req.params(":id"))

            val template: TemplateFile? = templateDao.get(id)

            if (template == null) {
                res.status(404)
                ""
            } else {
                res.setJsonContentType()
                jsonTransformer.toJson(template)
            }
        }
        post("") { req, res ->
            val template = jsonTransformer.fromJson(req.body(), TemplateFile::class.java)

            if (template.extension != null) {
                template.id = UUID.randomUUID()
                templateDao.save(template)

                res.status(201)
                res.setJsonContentType()
                res.header("Location", "$baseUrl/images/${template.id}.${template.extension}")
                jsonTransformer.toJson(template)
            } else {
                res.status(400)
                ""
            }
        }
    }
}