package com.github.eoinf.jiggen.endpoint

import com.github.eoinf.jiggen.dao.IImageDao
import org.apache.logging.log4j.LogManager
import spark.Spark.*
import java.io.File

private val logger = LogManager.getLogger()

fun imagesEndpoint(imageDao: IImageDao) {
    path("/images") {
        get("") { req, res ->
            logger.info("GET All request handled")
            "Please use the /templates or /backgrounds endpoints to upload images"
        }
        get("/:id") { req, res ->
            logger.info("GET request handled {}", req.params(":id"))
            val id = req.params(":id")

            val image: File? = imageDao.get(id)

            if (image == null) {
                res.status(404)
                ""
            } else {
                setPNGContentType(res)
                image.inputStream()
            }
        }

        put("/:id") {
            req, res ->
            logger.info("GET request handled {}", req.params(":id"))
            val id = req.params(":id")

            imageDao.save(id, req.raw().getPart("uploaded_file").inputStream)
            res.status(201)
        }
    }

}