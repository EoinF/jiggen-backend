package com.github.eoinf.jiggen.endpoint

import com.github.eoinf.jiggen.JsonTransformer
import com.github.eoinf.jiggen.dao.IBackgroundDao
import com.github.eoinf.jiggen.data.BackgroundFile
import com.github.eoinf.jiggen.data.TemplateFile
import org.apache.logging.log4j.LogManager
import spark.Spark
import spark.Spark.*
import java.util.*

private val logger = LogManager.getLogger()

fun backgroundsEndpoint(backgroundDao: IBackgroundDao, jsonTransformer: JsonTransformer, baseUrl: String) {
    path("/backgrounds") {
        get("") { req, res ->
            logger.info("GET All request handled")
            res.setJsonContentType()
            jsonTransformer.toJson(backgroundDao.get())
        }
        get("/:id") { req, res ->
            logger.info("GET request handled {}", req.params(":id"))
            val id = req.params(":id").toIntOrNull()

            val background: BackgroundFile? = backgroundDao.get(id)

            if (background == null) {
                res.status(404)
                ""
            } else {
                res.setJsonContentType()
                jsonTransformer.toJson(background)
            }
        }

        post("") { req, res ->
            val background = jsonTransformer.fromJson(req.body(), BackgroundFile::class.java)

            if (background.extension != null) {
                background.imageId = "tm" + UUID.randomUUID().toString()
                backgroundDao.save(background)
                res.status(201)
                res.setJsonContentType()
                res.header("Location", "$baseUrl/images/${background.imageId}.${background.extension}")
                jsonTransformer.toJson(background)
            } else {
                res.status(400)
                ""
            }
        }
    }
}