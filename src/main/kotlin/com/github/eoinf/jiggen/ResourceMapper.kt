package com.github.eoinf.jiggen

import com.github.eoinf.jiggen.config.JiggenConfig
import com.github.eoinf.jiggen.data.AtlasFile
import com.github.eoinf.jiggen.data.BackgroundFile
import com.github.eoinf.jiggen.data.GeneratedTemplate
import com.github.eoinf.jiggen.data.ImageFile
import com.github.eoinf.jiggen.data.PlayablePuzzle
import com.github.eoinf.jiggen.data.TemplateFile
import org.springframework.stereotype.Service
import java.util.*

@Service
class ResourceMapper(jiggenConfiguration: JiggenConfig) {
    private val baseUrl = jiggenConfiguration.baseUrl

    val backgroundsUrl = "$baseUrl/${BackgroundFile.RESOURCE_NAME}"
    val templatesUrl = "$baseUrl/${TemplateFile.RESOURCE_NAME}"
    val imagesUrl = "$baseUrl/${ImageFile.RESOURCE_NAME}"
    val atlasesUrl = "$baseUrl/${AtlasFile.RESOURCE_NAME}"
    val generatedTemplatesUrl = "$baseUrl/${GeneratedTemplate.RESOURCE_NAME}"
    val playablePuzzlesUrl = "$baseUrl/${PlayablePuzzle.RESOURCE_NAME}"
    val todaysPuzzlesUrl = "$playablePuzzlesUrl/today"

    fun backgroundsUrl(id: UUID): String {
        return "$backgroundsUrl/$id"
    }
    fun templatesUrl(id: UUID, relation: String? = null): String {
        return if (relation != null) {
            "$templatesUrl/$id/$relation"
        } else {
            "$templatesUrl/$id"
        }
    }

    fun puzzleTemplatesUrl(id: UUID): String {
        return "$generatedTemplatesUrl/$id"
    }

    fun imagesUrl(id: UUID, extension: String, offset: Int = 1): String {
        val offsetString = if (offset > 1) offset.toString() else ""
        return "$imagesUrl/$id$offsetString.$extension"
    }
    fun atlasUrl(id: UUID): String {
        return "$atlasesUrl/$id.atlas"
    }

    fun playablePuzzlesUrl(id: UUID): String {
        return "$playablePuzzlesUrl/$id"
    }
}