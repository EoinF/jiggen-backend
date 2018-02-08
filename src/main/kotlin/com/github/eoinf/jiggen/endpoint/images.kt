package com.github.eoinf.jiggen.endpoint

import com.github.eoinf.jiggen.JsonTransformer
import com.github.eoinf.jiggen.dao.IImageDao
import org.apache.logging.log4j.LogManager
import spark.Spark.*
import java.io.File
import java.util.*

private val logger = LogManager.getLogger()

fun imagesEndpoint(imageDao: IImageDao, jsonTransformer: JsonTransformer) {
    path("/images") {

        exception(IndexOutOfBoundsException::class.java) { e, req, res ->
            e.printStackTrace()
            res.status(400)
            res.body(jsonTransformer.toJson(
                    mapOf("error" to "" +
                            """Expected path parameter in the format <id>.<extension>
                                e.g. /images/1234.png
                            """
                    ))
            )
        }

        get("") { req, res ->
            logger.info("GET All request handled")
            "Please use the /templates or /backgrounds endpoints to upload images"
        }
        get("/:file") { req, res ->
            logger.info("GET request handled {}", req.params(":file"))

            val fileParts = req.params(":file")
                    .split('.')
            val id = UUID.fromString(fileParts[0])
            val ext = fileParts[1]

            val image: File? = imageDao.get(id, ext)

            if (image == null) {
                res.status(404)
                ""
            } else {
                res.setImageContentType(ext)
                image.inputStream()
            }
        }

        put("/:file") {
            req, res ->
            logger.info("PUT request handled {}", req.params(":file"))

            val fileParts = req.params(":file")
                    .split('.')
            val id = UUID.fromString(fileParts[0])
            val ext = fileParts[1]

            imageDao.save(id, ext, req.raw().inputStream)
            res.status(201)
            ""
        }
    }

}