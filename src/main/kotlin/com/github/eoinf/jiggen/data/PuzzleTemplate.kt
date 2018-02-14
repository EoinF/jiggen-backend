package com.github.eoinf.jiggen.data

import java.util.*
import javax.persistence.*

class PuzzleTemplate(
        @Id
        var id: UUID,

        @OneToMany
        @JoinColumn(name="id")
        var templateFile: TemplateFile
) {
    var atlasDetails: String? = null
}