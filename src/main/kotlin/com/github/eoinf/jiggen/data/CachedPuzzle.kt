package com.github.eoinf.jiggen.data

import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class CachedPuzzle : Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    var id: Int? = null

    @Basic
    var template: TemplateFile? = null

    @Temporal(javax.persistence.TemporalType.DATE)
    var created: LocalDateTime? = LocalDateTime.now()
}