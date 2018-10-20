package com.github.eoinf.jiggen.endpoint

import com.github.eoinf.jiggen.JsonTransformer
import com.github.eoinf.jiggen.ResourceMapper
import spark.Spark.get

fun baseEndpoint(resourceMapper: ResourceMapper, jsonTransformer: JsonTransformer) {
    get("/") { _, res ->
        res.setJsonContentType()
        jsonTransformer.toJson(mapOf(
                "description" to "This resource manages templates and cached puzzles for the jiggen game",
                "links" to mapOf(
                        "backgrounds" to resourceMapper.backgroundsUrl,
                        "templates" to resourceMapper.templatesUrl,
                        "generatedTemplates" to resourceMapper.generatedTemplatesUrl,
                        "playablePuzzles" to resourceMapper.playablePuzzlesUrl,
                        "todaysPuzzles" to resourceMapper.todaysPuzzlesUrl
                )
        ))
    }
}