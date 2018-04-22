package com.github.eoinf.jiggen

import com.github.eoinf.jiggen.data.BackgroundFile
import com.github.eoinf.jiggen.data.ImageFile
import com.github.eoinf.jiggen.data.PuzzleTemplate
import com.github.eoinf.jiggen.data.TemplateFile
import org.springframework.stereotype.Service
import java.util.*

@Service
class ResourceMapper(jiggenConfiguration: JiggenConfiguration) {
    private val baseUrl = jiggenConfiguration.baseUrl

    val backgroundsUrl = "$baseUrl/${BackgroundFile.RESOURCE_NAME}"
    val templatesUrl = "$baseUrl/${TemplateFile.RESOURCE_NAME}"
    val imagesUrl = "$baseUrl/${ImageFile.RESOURCE_NAME}"
    val puzzleTemplatesUrl = "$baseUrl/${PuzzleTemplate.RESOURCE_NAME}"

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
        return "$puzzleTemplatesUrl/$id"
    }

    fun imagesUrl(id: UUID, extension: String): String {
        return "$imagesUrl/$id.$extension"
    }
}