package com.github.eoinf.jiggen.data

import java.io.Serializable
import java.util.*
import javax.persistence.Basic
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

class BackgroundFile(
        @Basic
        @Id
        var id: UUID
) : Serializable {
    var name: String? = null
    var extension: String? = null
}
