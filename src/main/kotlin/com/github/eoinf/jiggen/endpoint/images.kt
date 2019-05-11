package com.github.eoinf.jiggen.endpoint

import com.github.eoinf.jiggen.JsonTransformer
import com.github.eoinf.jiggen.ResourceMapper
import com.github.eoinf.jiggen.dao.ImageDao
import com.github.eoinf.jiggen.data.ImageFile
import com.github.eoinf.jiggen.exception.NoMatchingResourceEntryException
import org.apache.logging.log4j.LogManager
import org.eclipse.jetty.http.HttpStatus
import org.springframework.stereotype.Controller
import spark.Spark.exception
import spark.Spark.get
import spark.Spark.path
import spark.Spark.put
import java.io.File
import java.io.FileNotFoundException
import java.util.UUID

private val logger = LogManager.getLogger()
private const val IMAGES = ImageFile.RESOURCE_NAME

@Controller
class ImageController(imageDao: IImageDao, jsonTransformer: JsonTransformer, resourceMapper: ResourceMapper) {
    init {
        path("/$IMAGES") {
            exception(IndexOutOfBoundsException::class.java) { e, req, res ->
                logger.error("/$IMAGES endpoint: ", e)
                res.status(HttpStatus.BAD_REQUEST_400)


                res.body(res.setupJsonResponse(
                        mapOf("error" to
                                "Expected path parameter in the format \"<id>.<extension>\" e.g. /IMAGES/1234.png"
                        ), jsonTransformer)
                )
            }
            exception(FileNotFoundException::class.java) { e, req, res ->
                logger.error("/$IMAGES endpoint: ", e)
                res.status(HttpStatus.NOT_FOUND_404)
                res.body(null)
            }
            exception(NoMatchingResourceEntryException::class.java) { e, req, res ->
                logger.error("/$IMAGES endpoint: ", e)
                res.status(HttpStatus.BAD_REQUEST_400)

                res.body(res.setupJsonResponse(
                        mapOf(
                                "error" to "Expected corresponding resource to exist. Use /templates or " +
                                        "/backgrounds endpoint to create the metadata first",
                                "links" to mapOf(
                                        "templates" to resourceMapper.templatesUrl,
                                        "backgrounds" to resourceMapper.backgroundsUrl
                                )
                        ), jsonTransformer)
                )
            }
            get("/:file") { req, res ->
                logger.info("GET request handled {}", req.params(":file"))

                val fileParts = req.params(":file")
                        .split('.')
                val id = fileParts[0]
                val ext = fileParts[1]

                val image: File? = imageDao.get(id, ext)


                if (image != null) {
                    res.header("Content-Length", image.length().toString())
                    res.setImageContentType(ext)
                    image.inputStream()
                } else {
                    res.status(HttpStatus.NOT_FOUND_404)
                }
            }

            put("/:file") { req, res ->
                logger.info("PUT request handled {}", req.params(":file"))

                val fileParts = req.params(":file")
                        .split('.')
                val id = UUID.fromString(fileParts[0])
                val ext = fileParts[1]

                if (req.contentLength() == 0) {
                    res.status(HttpStatus.BAD_REQUEST_400)
                    res.setupJsonResponse(mapOf("error" to "Image size must not be 0 bytes"), jsonTransformer)
                } else {
                    req.raw().inputStream.use {
                        imageDao.save(req, id, ext, it)
                    }
                    res.status(HttpStatus.CREATED_201)
                }
                ""
            }
        }
    }
}