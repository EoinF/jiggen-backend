package com.github.eoinf.jiggen

import com.github.eoinf.jiggen.config.JiggenConfig
import com.github.eoinf.jiggen.data.AtlasFile
import com.github.eoinf.jiggen.data.BackgroundFile
import com.github.eoinf.jiggen.data.GeneratedTemplate
import com.github.eoinf.jiggen.data.ImageFile
import com.github.eoinf.jiggen.data.PlayablePuzzle
import com.github.eoinf.jiggen.data.TemplateFile
import org.springframework.stereotype.Service
import spark.Request
import java.util.UUID

@Service
class ResourceMapper(val jiggenConfig: JiggenConfig) {
    val backgroundsUrl = BackgroundFile.RESOURCE_NAME
    val templatesUrl = TemplateFile.RESOURCE_NAME
    val imagesUrl = ImageFile.RESOURCE_NAME
    val atlasesUrl = AtlasFile.RESOURCE_NAME
    val generatedTemplatesUrl = GeneratedTemplate.RESOURCE_NAME
    val playablePuzzlesUrl = PlayablePuzzle.RESOURCE_NAME
    val todaysPuzzlesUrl = "$playablePuzzlesUrl/today"

    fun backgroundsUrl(request: Request, id: UUID): String {
        return "${baseUrl(request)}/$backgroundsUrl/$id"
    }
    fun templatesUrl(request: Request, id: UUID, relation: String? = null): String {
        return if (relation != null) {
            "${baseUrl(request)}/$templatesUrl/$id/$relation"
        } else {
            "${baseUrl(request)}/$templatesUrl/$id"
        }
    }

    fun baseUrl(request: Request) = "${jiggenConfig.protocol}://${request.host()}"

    fun puzzleTemplatesUrl(request: Request, id: UUID): String {
        return "${baseUrl(request)}/$generatedTemplatesUrl/$id"
    }

    fun imagesUrl(request: Request, id: UUID, extension: String, offset: String = ""): String {
        return "${baseUrl(request)}/$imagesUrl/$id$offset.$extension"
    }
    fun atlasUrl(request: Request, id: UUID): String {
        return "${baseUrl(request)}/$atlasesUrl/$id.atlas"
    }

    fun playablePuzzlesUrl(request: Request, id: UUID): String {
        return "${baseUrl(request)}/$playablePuzzlesUrl/$id"
    }
}