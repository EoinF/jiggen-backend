package com.github.eoinf.jiggen.endpoint

import com.github.eoinf.jiggen.JsonTransformer
import com.github.eoinf.jiggen.ResourceMapper
import org.springframework.stereotype.Controller
import spark.Spark.get


@Controller
class BaseApiController(resourceMapper: ResourceMapper, jsonTransformer: JsonTransformer) {
    init {
        get("/") { req, res ->
            val baseUrl = resourceMapper.baseUrl(req)


            res.setupJsonResponse(mapOf(
                    "description" to "This resource manages templates and cached puzzles for the jiggen game",
                    "links" to mapOf(
                            "backgrounds" to "$baseUrl/${resourceMapper.backgroundsUrl}",
                            "templates" to "$baseUrl/${resourceMapper.templatesUrl}",
                            "generatedTemplates" to "$baseUrl/${resourceMapper.generatedTemplatesUrl}",
                            "playablePuzzles" to "$baseUrl/${resourceMapper.playablePuzzlesUrl}",
                            "todaysPuzzles" to "$baseUrl/${resourceMapper.todaysPuzzlesUrl}"
                    )
            ), jsonTransformer)
        }
    }
}