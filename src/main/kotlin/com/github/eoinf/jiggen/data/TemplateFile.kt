package com.github.eoinf.jiggen.data

import java.io.Serializable
import java.util.*
import javax.persistence.Basic
import javax.persistence.Id

class TemplateFile(
        @Basic
        @Id
        var id: UUID
) : Serializable {
    var name: String? = null
    var extension: String? = null
}
