package com.github.eoinf.jiggen.data

import java.util.*
import javax.persistence.*

@Entity
class PuzzleTemplate(
        @Id
        @Column(columnDefinition = "BINARY(16)")
        private var id: UUID? = null,
        // The string used by Jiggen to identify the relative location of each
        // puzzle piece so the puzzle can be solved
        @Lob var puzzleDetails: String? = null,
        val extension: String? = null
) : EntityWithId {
    override fun getId(): UUID {
        return id!!
    }

    @ManyToOne
    @JoinColumn(name = "templateId", unique = true, nullable = false)
    var templateFile: TemplateFile? = null

    constructor(puzzleTemplateDTO: PuzzleTemplateDTO) : this(puzzleTemplateDTO.id, puzzleTemplateDTO.puzzleDetails,
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
                             var puzzleDetails: String? = null,
                             val extension: String? = null,
                             val links: Map<String, String>? = null)
