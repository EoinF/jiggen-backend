package com.github.eoinf.jiggen.data

import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class FinishedPuzzle : Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    var id: Int? = null

    @Basic
    var template: TemplateFile? = null

    @Basic
    var background: BackgroundFile? = null

    @Temporal(javax.persistence.TemporalType.DATE)
    var created: LocalDateTime? = LocalDateTime.now()
}