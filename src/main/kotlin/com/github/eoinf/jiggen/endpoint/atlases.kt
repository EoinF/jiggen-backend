package com.github.eoinf.jiggen.endpoint

import com.github.eoinf.jiggen.JsonTransformer
import com.github.eoinf.jiggen.ResourceMapper
import com.github.eoinf.jiggen.dao.AtlasDao
import com.github.eoinf.jiggen.data.AtlasFile
import org.apache.logging.log4j.LogManager
import org.eclipse.jetty.http.HttpStatus
import org.springframework.stereotype.Controller
import spark.Spark.exception
import spark.Spark.get
import spark.Spark.path
import java.io.FileNotFoundException
import java.util.UUID

private val logger = LogManager.getLogger()
private const val ATLASES = AtlasFile.RESOURCE_NAME

@Controller
class AtlasController(val atlasDao: AtlasDao, val jsonTransformer: JsonTransformer, val resourceMapper: ResourceMapper) {
    init {
        path("/$ATLASES") {
            exception(IndexOutOfBoundsException::class.java) { e, req, res ->
                logger.error("/$ATLASES endpoint: ", e)
                res.status(HttpStatus.BAD_REQUEST_400)

                res.body(res.setupJsonResponse(
                        mapOf("error" to
                                "Expected path parameter in the format \"<id>.<extension>\" e.g. /atlases/1234.atlas"
                        ), jsonTransformer)
                )
            }
            exception(FileNotFoundException::class.java) { e, req, res ->
                logger.error("/$ATLASES endpoint: ", e)
                res.status(HttpStatus.NOT_FOUND_404)
                res.body(null)
            }

            get("") { req, res ->
                logger.info("GET All request handled")
                res.setupJsonResponse(mapOf(
                        "description" to "Please use the /templates or /backgrounds endpoints to find atlases",
                        "links" to mapOf(
                                "templates" to resourceMapper.templatesUrl,
                                "backgrounds" to resourceMapper.backgroundsUrl
                        )
                ), jsonTransformer)
            }
            get("/:file") { req, res ->
                logger.info("GET request handled {}", req.params(":file"))

                val fileParts = req.params(":file")
                        .split('.')
                val id = UUID.fromString(fileParts[0])

                val file: AtlasFile? = atlasDao.get(id)

                if (file != null) {
                    res.setPlainTextContentType()
                    res.setGzipEncoding()
                    file.inputStream()
                } else {
                    res.status(HttpStatus.NOT_FOUND_404)
                }
            }
        }
    }
}