package com.github.eoinf.jiggen

import com.github.eoinf.jiggen.data.*
import org.springframework.stereotype.Service
import java.util.*

@Service
class DataMapper(private val resourceMapper: ResourceMapper) {

    fun toTemplateFileDTO(templateFile: TemplateFile, depth: Int = 0): TemplateFileDTO {
        val id: UUID = templateFile.getId()
        var name: String? = null
        var extension: String? = null
        var puzzleTemplates: Set<PuzzleTemplateDTO>? = null
        if (depth == 1) {
            name = templateFile.name
            extension = templateFile.extension
        } else if (depth > 1) {
            puzzleTemplates = templateFile.puzzleTemplates!!.map {
                toPuzzleTemplateDTO(it, depth = depth - 2)
            }.toSet()
        }

        return TemplateFileDTO(id, name, extension, puzzleTemplates, generateLinks(id))
    }

    private fun generateLinks(id: UUID): Map<String, String> {
        val childResourceName = PuzzleTemplate.RESOURCE_NAME
        return mapOf(
                "self" to resourceMapper.templatesUrl(id),
                "puzzleTemplates" to resourceMapper.templatesUrl(id, childResourceName)
        )
    }

    fun toPuzzleTemplateDTO(puzzleTemplate: PuzzleTemplate, depth: Int = 1): PuzzleTemplateDTO {
        val id = puzzleTemplate.getId()
        var atlasDetails: String? = null
        var templateFile: TemplateFileDTO? = null

        if (depth == 1) {
            atlasDetails = puzzleTemplate.atlasDetails
        } else if (depth > 1) {
            templateFile = toTemplateFileDTO(puzzleTemplate.templateFile!!, depth = depth - 2)
        }
        return PuzzleTemplateDTO(id, templateFile, atlasDetails, generateLinks(id, puzzleTemplate.templateFile))
    }

    private fun generateLinks(id: UUID, parentResource: EntityWithId?): Map<String, String> {
        val linksMap = mutableMapOf(
                "self" to resourceMapper.puzzleTemplatesUrl(id)
        )
        if (parentResource != null) {
            linksMap["templateFile"] = resourceMapper.templatesUrl(parentResource.getId())
        }
        return linksMap
    }

    fun toBackgroundFileDTO(backgroundFile: BackgroundFile, depth: Int = 0): BackgroundFileDTO {
        val id: UUID = backgroundFile.getId()
        var name: String? = null
        var extension: String? = null
        if (depth >= 1) {
            name = backgroundFile.name
            extension = backgroundFile.extension
        }

        return BackgroundFileDTO(id, name, extension, generateLinks1(id))
    }

    private fun generateLinks1(id: UUID): Map<String, String> {
        return mapOf(
                "self" to resourceMapper.backgroundsUrl(id)
        )
    }
}
