package com.github.eoinf.jiggen.endpoint

import com.github.eoinf.jiggen.JsonTransformer
import com.github.eoinf.jiggen.ResourceMapper
import spark.Spark.get

fun baseEndpoint(resourceMapper: ResourceMapper, jsonTransformer: JsonTransformer) {
    get("/") { req, res ->
        val baseUrl = "${req.scheme()}://${req.host()}"

        res.setJsonContentType()
        jsonTransformer.toJson(mapOf(
                "description" to "This resource manages templates and cached puzzles for the jiggen game",
                "links" to mapOf(
                        "backgrounds" to "$baseUrl/${resourceMapper.backgroundsUrl}",
                        "templates" to "$baseUrl/${resourceMapper.templatesUrl}",
                        "generatedTemplates" to "$baseUrl/${resourceMapper.generatedTemplatesUrl}",
                        "playablePuzzles" to "$baseUrl/${resourceMapper.playablePuzzlesUrl}",
                        "todaysPuzzles" to "$baseUrl/${resourceMapper.todaysPuzzlesUrl}"
                )
        ))
    }
}