package com.github.eoinf.jiggen.data

import java.util.*
import javax.persistence.*

@Entity
class PuzzleTemplate() {
    @ManyToOne
    @JoinColumn(name="templateId", unique = true, nullable = false)
    lateinit var templateFile: TemplateFile

    @Lob
    var atlasDetails: String? = null

    constructor(id: UUID, templateFile: TemplateFile): this() {
        this.id = id
        this.templateFile = templateFile
    }

    @Id
    lateinit var id: UUID
}

class PuzzleTemplateDTO(puzzleTemplate: PuzzleTemplate, depth: Int = 0) {
    val id: UUID?
    val templateFile: TemplateFileDTO?

    @Lob
    var atlasDetails: String?

    init {
        id = puzzleTemplate.id
        atlasDetails = puzzleTemplate.atlasDetails

        if (depth == 0) {
            templateFile = null
        } else {
            templateFile = TemplateFileDTO(puzzleTemplate.templateFile, depth=depth - 1)
        }
    }
}