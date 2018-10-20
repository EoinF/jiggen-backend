package com.github.eoinf.jiggen

import com.github.eoinf.jiggen.PuzzleExtractor.GraphEdge
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.IntRectangle
import com.github.eoinf.jiggen.config.JiggenConfig
import com.github.eoinf.jiggen.data.*
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

private val logger = LogManager.getLogger()

@Service
class DataMapper(private val resourceMapper: ResourceMapper, private val jiggenConfiguration: JiggenConfig) {

    fun toTemplateFileDTO(templateFile: TemplateFile, depth: Int = 0): TemplateFileDTO {
        val id: UUID = templateFile.getId()
        val name = templateFile.name
        var extension: String? = null
        var puzzleTemplates: Set<GeneratedTemplateDTO>? = null

        var linksMap: Map<String, String>? = null

        if (depth >= 1) {
            extension = templateFile.extension
        }
        if (depth > 1) {
            puzzleTemplates = templateFile.puzzleTemplates!!.map {
                toGeneratedTemplateDTO(it, depth = depth - 2)
            }.toSet()
        }

        if (depth > 0) {
            val childResourceName = GeneratedTemplate.RESOURCE_NAME
            linksMap = mutableMapOf(
                    "self" to resourceMapper.templatesUrl(id),
                    "generatedTemplates" to resourceMapper.templatesUrl(id, childResourceName)
            )

            if (imageExists(templateFile.getId(), templateFile.extension!!)) {
                linksMap["image"] = resourceMapper.imagesUrl(templateFile.getId(), templateFile.extension)
            }
        }
        return TemplateFileDTO(id, name, extension, puzzleTemplates, linksMap)
    }

    fun toGeneratedTemplateDTO(puzzleTemplate: GeneratedTemplate, depth: Int = 0): GeneratedTemplateDTO {
        val id = puzzleTemplate.getId()
        var vertices: Map<Int, IntRectangle>? = null
        var edges: List<GraphEdge>? = null
        var extension: String? = null
        var templateFile: TemplateFileDTO? = null
        var width: Int? = null
        var height: Int? = null

        var linksMap: Map<String, String>? = null

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

        if (depth > 0) {
            linksMap = mutableMapOf(
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
        }

        return GeneratedTemplateDTO(
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

        var linksMap: Map<String, String>? = null

        if (depth >= 1) {
            name = backgroundFile.name
            extension = backgroundFile.extension
        }

        if (depth > 0) {
            linksMap = mutableMapOf(
                    "self" to resourceMapper.backgroundsUrl(id)
            )

            if (imageExists(backgroundFile.getId(), backgroundFile.extension!!)) {
                linksMap["image"] = resourceMapper.imagesUrl(backgroundFile.getId(), backgroundFile.extension)
            }
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

    fun toPlayablePuzzleDTO(playablePuzzle: PlayablePuzzle, depth: Int = 0): PlayablePuzzleDTO {
        val id: UUID = playablePuzzle.getId()
        val name: String? = playablePuzzle.name

        var created: Date? = null
        var releaseDate: Date? = null
        var generatedTemplate: GeneratedTemplateDTO? = null
        var backgroundFile: BackgroundFileDTO? = null

        var linksMap: Map<String, String>? = null

        if (depth >= 1) {
            created = playablePuzzle.created
            releaseDate = playablePuzzle.releaseDate
        }

        if (depth > 1) {
            generatedTemplate = toGeneratedTemplateDTO(playablePuzzle.generatedTemplate!!, depth = depth - 2)
            backgroundFile = toBackgroundFileDTO(playablePuzzle.background!!, depth=depth-2)
        }

        if (depth > 0) {
            linksMap = mutableMapOf(
                    "self" to resourceMapper.playablePuzzlesUrl(id)
            )

            if (playablePuzzle.generatedTemplate != null) {
                linksMap["generatedTemplate"] = resourceMapper.puzzleTemplatesUrl(playablePuzzle.generatedTemplate!!.getId())
            }
            if (playablePuzzle.background != null) {
                linksMap["background"] = resourceMapper.backgroundsUrl(playablePuzzle.background!!.getId())
            }
        }

       val x =PlayablePuzzleDTO(id, name, generatedTemplate, backgroundFile, created, releaseDate, linksMap)
        logger.info("playablePuzzle: {}", x)

        return x
    }
}
