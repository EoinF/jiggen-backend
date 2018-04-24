package com.github.eoinf.jiggen.data

import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
class FinishedPuzzle: Serializable {
    @Id
    @Column(columnDefinition = "BINARY(16)")
    var id: UUID? = null

    @Temporal(javax.persistence.TemporalType.DATE)
    var created: Date? = Date()

    @ManyToOne
    @JoinColumn(name="templateId")
    var templateFile: TemplateFile? = null

    @ManyToOne
    @JoinColumn(name="backgroundId")
    var backgroundFile: BackgroundFile? = null
}