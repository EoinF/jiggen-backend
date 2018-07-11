package com.github.eoinf.jiggen

import com.github.eoinf.jiggen.PuzzleExtractor.GraphEdge
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.IntRectangle
import com.github.eoinf.jiggen.config.JiggenConfig
import com.github.eoinf.jiggen.data.*
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

@Service
class DataMapper(private val resourceMapper: ResourceMapper, private val jiggenConfiguration: JiggenConfig) {

    fun toTemplateFileDTO(templateFile: TemplateFile, depth: Int = 0): TemplateFileDTO {
        val id: UUID = templateFile.getId()
        val name = templateFile.name
        var extension: String? = null
        var puzzleTemplates: Set<PuzzleTemplateDTO>? = null
        if (depth >= 1) {
            extension = templateFile.extension
        }
        if (depth > 1) {
            puzzleTemplates = templateFile.puzzleTemplates!!.map {
                toPuzzleTemplateDTO(it, depth = depth - 2)
            }.toSet()
        }

        val childResourceName = PuzzleTemplate.RESOURCE_NAME
        val linksMap = mutableMapOf(
                "self" to resourceMapper.templatesUrl(id),
                "generatedTemplates" to resourceMapper.templatesUrl(id, childResourceName)
        )

        if (imageExists(templateFile.getId(), templateFile.extension!!)) {
            linksMap["image"] = resourceMapper.imagesUrl(templateFile.getId(), templateFile.extension)
        }

        return TemplateFileDTO(id, name, extension, puzzleTemplates, linksMap)
    }

    fun toPuzzleTemplateDTO(puzzleTemplate: PuzzleTemplate, depth: Int = 0): PuzzleTemplateDTO {
        val id = puzzleTemplate.getId()
        var vertices: Map<Int, IntRectangle>? = null
        var edges: List<GraphEdge>? = null
        var extension: String? = null
        var templateFile: TemplateFileDTO? = null
        var width: Int? = null
        var height: Int? = null

        if (depth >= 1) {
            width = puzzleTemplate.width
            height = puzzleTemplate.height
            vertices = puzzleTemplate.vertices
            edges = puzzleTemplate.edges
            extension = puzzleTemplate.extension
        }
        if (depth > 1) {
            templateFile = toTemplateFileDTO(puzzleTemplate.templateFile!!, depth = depth - 2)
        }

        val linksMap = mutableMapOf(
                "self" to resourceMapper.puzzleTemplatesUrl(id)
        )
        if (imageExists(puzzleTemplate.getId(), puzzleTemplate.extension!!)) {
            linksMap["image"] = resourceMapper.imagesUrl(puzzleTemplate.getId(), puzzleTemplate.extension)
        }
        if (atlasExists(puzzleTemplate.getId())) {
            linksMap["atlas"] = resourceMapper.atlasUrl(puzzleTemplate.getId())
        }
        if (puzzleTemplate.templateFile != null) {
            linksMap["templateFile"] = resourceMapper.templatesUrl(puzzleTemplate.templateFile!!.getId())
        }

        return PuzzleTemplateDTO(
                id = id,
                templateFile = templateFile,
                width = width,
                height = height,
                vertices = vertices,
                edges = edges,
                extension = extension,
                links = linksMap
        )
    }


    fun toBackgroundFileDTO(backgroundFile: BackgroundFile, depth: Int = 0): BackgroundFileDTO {
        val id: UUID = backgroundFile.getId()
        var name: String? = null
        var extension: String? = null
        if (depth >= 1) {
            name = backgroundFile.name
            extension = backgroundFile.extension
        }

        val linksMap = mutableMapOf(
                "self" to resourceMapper.backgroundsUrl(id)
        )

        if (imageExists(backgroundFile.getId(), backgroundFile.extension!!)) {
            linksMap["image"] = resourceMapper.imagesUrl(backgroundFile.getId(), backgroundFile.extension)
        }

        return BackgroundFileDTO(id, name, extension, linksMap)
    }

    private fun imageExists(id: UUID, extension: String): Boolean {
        val pathname = "${jiggenConfiguration.imageFolder}/$id.$extension"
        return Files.exists(Paths.get(pathname))
    }

    private fun atlasExists(id: UUID): Boolean {
        val pathname = "${jiggenConfiguration.atlasFolder}/$id.atlas"
        return Files.exists(Paths.get(pathname))
    }
}
