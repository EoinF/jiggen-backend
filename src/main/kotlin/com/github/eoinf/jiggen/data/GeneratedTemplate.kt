package com.github.eoinf.jiggen.data

import java.util.*
import javax.persistence.*

class GeneratedTemplate(
        var packedImageId: UUID,
        @Id
        @OneToOne
        @JoinColumn(name="imageId")
        var templateFile: TemplateFile
) {
    var extension : String? = null
    var atlasDetails: String? = null

}