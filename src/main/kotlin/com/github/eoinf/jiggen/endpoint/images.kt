package com.github.eoinf.jiggen.endpoint

import com.github.eoinf.jiggen.JsonTransformer
import com.github.eoinf.jiggen.dao.IImageDao
import com.github.eoinf.jiggen.dao.NoMatchingResourceEntryException
import org.apache.logging.log4j.LogManager
import org.eclipse.jetty.http.HttpStatus
import spark.Spark.*
import java.io.File
import java.io.FileNotFoundException
import java.util.*

private val logger = LogManager.getLogger()

fun imagesEndpoint(imageDao: IImageDao, jsonTransformer: JsonTransformer) {
    path("/images") {
        exception(IndexOutOfBoundsException::class.java) { e, req, res ->
            logger.error("/images endpoint: ", e)
            res.status(HttpStatus.BAD_REQUEST_400)

            res.setJsonContentType()
            res.body(jsonTransformer.toJson(
                    mapOf("error" to
                            "Expected path parameter in the format \"<id>.<extension>\" e.g. /images/1234.png"
                    ))
            )
        }
        exception(FileNotFoundException::class.java) { e, req, res ->
            logger.error("/images endpoint: ", e)
            res.status(HttpStatus.NOT_FOUND_404)
            res.body(null)
        }
        exception(NoMatchingResourceEntryException::class.java) { e, req, res ->
            logger.error("/images endpoint: ", e)
            res.status(HttpStatus.BAD_REQUEST_400)
            res.setJsonContentType()
            res.body(jsonTransformer.toJson(
                    mapOf("error" to "" +
                            "Expected corresponding resource to exist. Use /templates or " +
                            "/backgrounds endpoint to create the metadata first"
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

            val image: File = imageDao.get(id, ext)

            res.setImageContentType(ext)
            image.inputStream()
        }

        put("/:file") { req, res ->
            logger.info("PUT request handled {}", req.params(":file"))

            val fileParts = req.params(":file")
                    .split('.')
            val id = UUID.fromString(fileParts[0])
            val ext = fileParts[1]

            if (req.contentLength() == 0) {
                res.status(HttpStatus.BAD_REQUEST_400)
            } else {
                req.raw().inputStream.use {
                    imageDao.save(id, ext, it)
                }
                res.status(HttpStatus.CREATED_201)
            }
            ""
        }
    }

}