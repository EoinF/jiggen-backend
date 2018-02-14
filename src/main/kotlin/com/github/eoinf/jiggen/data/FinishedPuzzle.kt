package com.github.eoinf.jiggen.data

import java.io.Serializable
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
class FinishedPuzzle(
        @Id
        var id: UUID
) : Serializable {
    @Basic
    var template: TemplateFile? = null

    @Basic
    var background: BackgroundFile? = null

    @Temporal(javax.persistence.TemporalType.DATE)
    var created: LocalDateTime? = LocalDateTime.now()
}