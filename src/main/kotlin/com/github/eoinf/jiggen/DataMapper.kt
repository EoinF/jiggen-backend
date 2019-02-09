package com.github.eoinf.jiggen

import com.github.eoinf.jiggen.PuzzleExtractor.GraphEdge
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.IntRectangle
import com.github.eoinf.jiggen.config.JiggenConfig
import com.github.eoinf.jiggen.data.BackgroundFile
import com.github.eoinf.jiggen.data.BackgroundFileDTO
import com.github.eoinf.jiggen.data.GeneratedTemplate
import com.github.eoinf.jiggen.data.GeneratedTemplateDTO
import com.github.eoinf.jiggen.data.PlayablePuzzle
import com.github.eoinf.jiggen.data.PlayablePuzzleDTO
import com.github.eoinf.jiggen.data.TemplateFile
import com.github.eoinf.jiggen.data.TemplateFileDTO
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Service
import spark.Request
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

private val logger = LogManager.getLogger()

@Service
class DataMapper(private val resourceMapper: ResourceMapper, private val jiggenConfiguration: JiggenConfig) {

    fun toTemplateFileDTO(request: Request, templateFile: TemplateFile, isEmbedded: Boolean): TemplateFileDTO {
        val id: UUID = templateFile.getId()
        val name = templateFile.name
        val extension: String? = templateFile.extension

        val childResourceName = GeneratedTemplate.RESOURCE_NAME
        val linksMap = mutableMapOf(
                "self" to resourceMapper.templatesUrl(request, id),
                "generatedTemplates" to resourceMapper.templatesUrl(request, id, childResourceName)
        )

        if (imageExists(templateFile.getId(), templateFile.extension!!)) {
            linksMap["image"] = resourceMapper.imagesUrl(request, templateFile.getId(), templateFile.extension)
        }

        return TemplateFileDTO(id, name, extension, linksMap)
    }

    fun toGeneratedTemplateDTO(request: Request, puzzleTemplate: GeneratedTemplate, isEmbedded: Boolean): GeneratedTemplateDTO {
        val id = puzzleTemplate.getId()
        var vertices: Map<Int, IntRectangle>? = null
        var edges: List<GraphEdge>? = null
        val extension: String? = puzzleTemplate.extension
        val width: Int? = puzzleTemplate.width
        val height: Int? = puzzleTemplate.height

        if (!isEmbedded) {
            vertices = puzzleTemplate.vertices
            edges = puzzleTemplate.edges
        }

        val linksMap = mutableMapOf<String, Any>(
                "self" to resourceMapper.puzzleTemplatesUrl(request, id)
        )

        if (imageExists(puzzleTemplate.getId(), puzzleTemplate.extension!!)) {
            linksMap["image"] = resourceMapper.imagesUrl(request, puzzleTemplate.getId(), puzzleTemplate.extension)
        }
        val imageLinks = mutableListOf<String>()
        var offset = 1
        while (imageExists(puzzleTemplate.getId(), puzzleTemplate.extension, offset)) {
            imageLinks.add(resourceMapper.imagesUrl(request, puzzleTemplate.getId(), puzzleTemplate.extension, offset))
            offset++
        }
        linksMap["images"] = imageLinks.toTypedArray()

        if (atlasExists(puzzleTemplate.getId())) {
            linksMap["atlas"] = resourceMapper.atlasUrl(request, puzzleTemplate.getId())
        }
        if (puzzleTemplate.templateFile != null) {
            linksMap["templateFile"] = resourceMapper.templatesUrl(request, puzzleTemplate.templateFile!!.getId())
        }

        return GeneratedTemplateDTO(
                id = id,
                width = width,
                height = height,
                vertices = vertices,
                edges = edges,
                extension = extension,
                links = linksMap
        )
    }


    fun toBackgroundFileDTO(request: Request, backgroundFile: BackgroundFile, isEmbedded: Boolean): BackgroundFileDTO {
        val id: UUID = backgroundFile.getId()
        val name: String? = backgroundFile.name
        val extension: String? = backgroundFile.extension

        val linksMap = mutableMapOf(
                "self" to resourceMapper.backgroundsUrl(request, id)
        )

        if (imageExists(backgroundFile.getId(), backgroundFile.extension!!)) {
            linksMap["image"] = resourceMapper.imagesUrl(request, backgroundFile.getId(), backgroundFile.extension)
        }

        return BackgroundFileDTO(id, name, extension, null, linksMap)
    }

    private fun imageExists(id: UUID, extension: String, offset: Int = 1): Boolean {
        val offsetString = if (offset > 1) offset.toString() else ""
        val pathname = "${jiggenConfiguration.imageFolder}/$id$offsetString.$extension"
        return Files.exists(Paths.get(pathname))
    }

    private fun atlasExists(id: UUID): Boolean {
        val pathname = "${jiggenConfiguration.atlasFolder}/$id.atlas"
        return Files.exists(Paths.get(pathname))
    }

    fun toPlayablePuzzleDTO(request: Request, playablePuzzle: PlayablePuzzle, isEmbedded: Boolean): PlayablePuzzleDTO {
        val id: UUID = playablePuzzle.getId()
        val name: String? = playablePuzzle.name

        var created: Date? = null
        var releaseDate: Date? = null

        if (!isEmbedded) {
            created = playablePuzzle.created
            releaseDate = playablePuzzle.releaseDate
        }

        val linksMap = mutableMapOf(
                "self" to resourceMapper.playablePuzzlesUrl(request, id)
        )

        if (playablePuzzle.generatedTemplate != null) {
            linksMap["generatedTemplate"] = resourceMapper.puzzleTemplatesUrl(request, playablePuzzle.generatedTemplate!!.getId())
        }
        if (playablePuzzle.background != null) {
            linksMap["background"] = resourceMapper.backgroundsUrl(request, playablePuzzle.background!!.getId())
        }


       return PlayablePuzzleDTO(id, name, null, null, created, releaseDate, linksMap)
    }
}
