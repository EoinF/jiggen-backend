package com.github.eoinf.jiggen.data

import com.github.eoinf.jiggen.PuzzleExtractor.GraphEdge
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.IntRectangle
import com.github.eoinf.jiggen.dao.EdgeListConverter
import com.github.eoinf.jiggen.dao.VertexDefinitionListConverter
import java.util.*
import javax.persistence.Column
import javax.persistence.Convert
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.Lob
import javax.persistence.ManyToOne

@Entity
class GeneratedTemplate(
        @Id
        @Column(columnDefinition = "BINARY(16)")
        private var id: UUID? = null,
        val width: Int? = null,
        val height: Int? = null,
        // The rectangle specifications of each vertex in the puzzle
        @Convert(converter = VertexDefinitionListConverter::class)
        @Lob var vertices: Map<Int, IntRectangle>? = null,
        // The pairs of vertices that make up the edges of the graph
        @Convert(converter = EdgeListConverter::class)
        @Lob var edges: List<GraphEdge>? = null,
        val extension: String? = null
) : EntityWithId {
    override fun getId(): UUID {
        return id!!
    }

    @ManyToOne
    @JoinColumn(name = "templateId", unique = true, nullable = false)
    var templateFile: TemplateFile? = null

    constructor(generatedTemplateDTO: GeneratedTemplateDTO) : this(generatedTemplateDTO.id, generatedTemplateDTO.width,
            generatedTemplateDTO.height, generatedTemplateDTO.vertices, generatedTemplateDTO.edges, generatedTemplateDTO.extension) {
        if (generatedTemplateDTO.templateFileId != null) {
            this.templateFile = TemplateFile(generatedTemplateDTO.templateFileId)
        }
    }

    companion object {
        const val RESOURCE_NAME = "generated-templates"
    }
}

data class GeneratedTemplateDTO(val id: UUID,
                                val templateFileId: UUID? = null,
                                val width: Int? = null,
                                val height: Int? = null,
                                @Lob var vertices: Map<Int, IntRectangle>? = null,
                                @Lob var edges: List<GraphEdge>? = null,
                                val extension: String? = null,
                                val links: Map<String, String>? = null)
