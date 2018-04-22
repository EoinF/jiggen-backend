package com.github.eoinf.jiggen.endpoint

import com.github.eoinf.jiggen.JsonTransformer
import com.github.eoinf.jiggen.ResourceMapper
import com.github.eoinf.jiggen.dao.IBackgroundDao
import com.github.eoinf.jiggen.data.BackgroundFile
import com.github.eoinf.jiggen.data.BackgroundFileDTO
import org.apache.logging.log4j.LogManager
import spark.Spark.*
import java.util.*

private val logger = LogManager.getLogger()

private const val backgrounds = BackgroundFile.RESOURCE_NAME

fun backgroundsEndpoint(backgroundDao: IBackgroundDao, jsonTransformer: JsonTransformer, resourceMapper: ResourceMapper) {
    path("/$backgrounds") {
        get("") { req, res ->
            logger.info("GET All request handled")
            res.setJsonContentType()
            jsonTransformer.toJson(backgroundDao.get())
        }
        get("/:id") { req, res ->
            logger.info("GET request handled {}", req.params(":id"))
            val id = UUID.fromString(req.params(":id"))

            val background: BackgroundFileDTO? = backgroundDao.get(id)

            if (background == null) {
                res.status(404)
                ""
            } else {
                res.setJsonContentType()
                jsonTransformer.toJson(background)
            }
        }

        post("") { req, res ->
            val background = jsonTransformer.fromJson(req.body(), BackgroundFileDTO::class.java)

            if (background.extension != null) {
                val result = backgroundDao.save(background.copy(id= UUID.randomUUID()))
                res.status(201)
                res.setJsonContentType()
                res.header("Location", resourceMapper.imagesUrl(result.id!!, background.extension))
                jsonTransformer.toJson(result)
            } else {
                res.status(400)
                ""
            }
        }
    }
}