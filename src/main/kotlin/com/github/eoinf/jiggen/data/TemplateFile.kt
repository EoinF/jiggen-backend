package com.github.eoinf.jiggen.data

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
data class TemplateFile(
        var name: String? = null,
        var extension: String? = null
) {
    @OneToMany
    //@JoinColumn(name="puzzleTemplates")
    lateinit var puzzleTemplates: List<PuzzleTemplate>
    constructor(id: UUID) : this() {
        this.id = id
    }

    @Id
    lateinit var id: UUID
}
