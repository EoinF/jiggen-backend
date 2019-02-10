package com.github.eoinf.jiggen.endpoint

import com.github.eoinf.jiggen.JsonTransformer
import com.github.eoinf.jiggen.ResourceMapper
import com.github.eoinf.jiggen.dao.IGeneratedTemplateDao
import com.github.eoinf.jiggen.dao.ITemplateDao
import com.github.eoinf.jiggen.data.GeneratedTemplate
import com.github.eoinf.jiggen.data.TemplateFile
import com.github.eoinf.jiggen.data.TemplateFileDTO
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Controller
import spark.Spark.get
import spark.Spark.path
import spark.Spark.post
import java.util.*

private val logger = LogManager.getLogger()

private const val puzzleTemplates = GeneratedTemplate.RESOURCE_NAME
private const val templates = TemplateFile.RESOURCE_NAME

@Controller
class TemplateController(templateDao: ITemplateDao, generatedTemplateDao: IGeneratedTemplateDao,
                      jsonTransformer: JsonTransformer, resourceMapper: ResourceMapper) {
    init {
        path("/$templates") {
            get("") { req, res ->
                logger.info("GET All request handled")
                res.setJsonContentType()
                jsonTransformer.toJson(templateDao.get(req))
            }
            get("/:id") { req, res ->
                logger.info("GET request handled {}", req.params(":id"))
                val id = UUID.fromString(req.params(":id"))

                val template: TemplateFileDTO? = templateDao.get(req, id)

                if (template == null) {
                    res.status(404)
                    ""
                } else {
                    res.setJsonContentType()
                    jsonTransformer.toJson(template)
                }
            }
            post("") { req, res ->
                logger.info("body: {}", req.body())
                val template = jsonTransformer.fromJson(req.body(), TemplateFileDTO::class.java)
                logger.info("POST request handled {}", template)

                if (template.extension != null) {
                    val result = templateDao.save(req, template.copy(id = UUID.randomUUID()))

                    res.status(201)
                    res.setJsonContentType()
                    res.header("Location", resourceMapper.imagesUrl(req, result.id!!, template.extension))
                    jsonTransformer.toJson(result)
                } else {
                    res.status(400)
                    ""
                }
            }
        }
        path("/$templates/:id/$puzzleTemplates") {
            get("") { req, res ->
                logger.info("GET request handled {}", req.params(":id"))
                val templateId = UUID.fromString(req.params(":id"))
                res.setJsonContentType()
                jsonTransformer.toJson(generatedTemplateDao.getByTemplateId(req, templateId))
            }
        }
    }
}