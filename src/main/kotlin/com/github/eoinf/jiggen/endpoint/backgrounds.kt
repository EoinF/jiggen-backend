package com.github.eoinf.jiggen.endpoint

import com.github.eoinf.jiggen.JsonTransformer
import com.github.eoinf.jiggen.ResourceMapper
import com.github.eoinf.jiggen.dao.IBackgroundDao
import com.github.eoinf.jiggen.data.BackgroundFile
import com.github.eoinf.jiggen.data.BackgroundFileDTO
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Controller
import spark.Spark.get
import spark.Spark.path
import spark.Spark.post
import java.util.*

private val logger = LogManager.getLogger()

private const val backgrounds = BackgroundFile.RESOURCE_NAME

@Controller
class BackgroundsController(private val backgroundDao: IBackgroundDao, private val jsonTransformer: JsonTransformer,
                            private val resourceMapper: ResourceMapper) {
    init {
        path("/$backgrounds") {
            get("") { req, res ->
                logger.info("GET All request handled")
                res.setJsonContentType()
                jsonTransformer.toJson(backgroundDao.get(req))
            }
            get("/:id") { req, res ->
                logger.info("GET request handled {}", req.params(":id"))
                val id = UUID.fromString(req.params(":id"))

                val background: BackgroundFileDTO? = backgroundDao.get(req, id)

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
                if (background.extension == null) {
                    res.status(400)
                    "\"extension\" parameter is required"
                } else if (background.name.isNullOrBlank()) {
                    res.status(400)
                    "\"name\" parameter is required"
                } else {
                    val result = backgroundDao.save(req, background.copy(id = UUID.randomUUID()))
                    res.status(201)
                    res.setJsonContentType()
                    res.header("Location", resourceMapper.imagesUrl(req, result.id!!, background.extension))
                    jsonTransformer.toJson(result)
                }
            }
        }
    }
}