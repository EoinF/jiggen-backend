package com.github.eoinf.jiggen.data

import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.IntRectangle
import com.github.eoinf.jiggen.dao.VertexDefinitionListConverter
import java.util.*
import javax.persistence.*

@Entity
class PuzzleTemplate(
        @Id
        @Column(columnDefinition = "BINARY(16)")
        private var id: UUID? = null,
        // The rectangle specifications of each vertex in the puzzle
        @Convert(converter = VertexDefinitionListConverter::class)
        @Lob var vertices: Map<Int, IntRectangle>? = null,
        val extension: String? = null
) : EntityWithId {
    override fun getId(): UUID {
        return id!!
    }

    @ManyToOne
    @JoinColumn(name = "templateId", unique = true, nullable = false)
    var templateFile: TemplateFile? = null

    constructor(puzzleTemplateDTO: PuzzleTemplateDTO) : this(puzzleTemplateDTO.id, puzzleTemplateDTO.vertices,
            puzzleTemplateDTO.extension) {
        if (puzzleTemplateDTO.templateFile != null) {
            this.templateFile = TemplateFile(puzzleTemplateDTO.templateFile.id)
        }
    }

    companion object {
        const val RESOURCE_NAME = "generated-templates"
    }
}

data class PuzzleTemplateDTO(val id: UUID,
                             val templateFile: TemplateFileDTO? = null,
                             @Lob var vertices: Map<Int, IntRectangle>? = null,
                             val extension: String? = null,
                             val links: Map<String, String>? = null)
