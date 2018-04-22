package com.github.eoinf.jiggen.data

import java.util.*
import javax.persistence.*

@Entity
class PuzzleTemplate(
        @Id
        private var id: UUID? = null,
        @Lob
        var atlasDetails: String? = null
) : EntityWithId {
    override fun getId(): UUID {
        return id!!
    }

    @ManyToOne
    @JoinColumn(name = "templateId", unique = true, nullable = false)
    var templateFile: TemplateFile? = null

    constructor(puzzleTemplateDTO: PuzzleTemplateDTO) : this(puzzleTemplateDTO.id, puzzleTemplateDTO.atlasDetails) {
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
                             val atlasDetails: String? = null,
                             val links: Map<String, String>? = null)
