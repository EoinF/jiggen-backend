package com.github.eoinf.jiggen.data

import java.util.*
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
data class TemplateFile(
        var name: String? = null,
        var extension: String? = null
) {
    @OneToMany(mappedBy = "templateFile", cascade= [(CascadeType.ALL)])
    lateinit var puzzleTemplates: Set<PuzzleTemplate>
    constructor(id: UUID) : this() {
        this.id = id
    }
    constructor(templateFileDTO: TemplateFileDTO) : this(templateFileDTO.name, templateFileDTO.extension) {
        this.id = templateFileDTO.id
    }

    @Id
    lateinit var id: UUID
}

class TemplateFileDTO(templateFile: TemplateFile, depth: Int=0) {
    var id: UUID = templateFile.id
    var name: String? = templateFile.name
    var extension: String? = templateFile.extension

    val puzzleTemplates: List<PuzzleTemplateDTO>?

    init {
        if (depth == 0) {
            puzzleTemplates = null
        } else{
            puzzleTemplates = templateFile.puzzleTemplates.map {
                PuzzleTemplateDTO(it, depth = depth-1)
            }
        }
    }
}