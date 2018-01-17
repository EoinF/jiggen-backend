package com.github.eoinf.jiggen.data

import javax.persistence.Basic
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import java.io.Serializable
import java.time.LocalDateTime

@Entity
class CachedPuzzle : Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    var id: Int? = null

    @Basic
    var template: TemplateFile? = null
    @Basic
    var background: BackgroundFile? = null
    @Basic
    var created: LocalDateTime? = null
}