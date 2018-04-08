package com.github.eoinf.jiggen.data

import java.util.*
import javax.persistence.*

@Entity
class PuzzleTemplate() {
    @ManyToOne
    @JoinColumn(name="templateId")
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