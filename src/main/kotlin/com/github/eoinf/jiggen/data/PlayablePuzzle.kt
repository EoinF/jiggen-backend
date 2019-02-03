package com.github.eoinf.jiggen.data

import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Temporal

@Entity
data class PlayablePuzzle(
        @Id
        @Column(columnDefinition = "BINARY(16)")
        private val id: UUID? = null,

        val name: String? = null,

        @Temporal(javax.persistence.TemporalType.TIMESTAMP)
        val releaseDate: Date? = null)
    : EntityWithId {

    @ManyToOne
    @JoinColumn(name = "templateId")
    var generatedTemplate: GeneratedTemplate? = null
    @ManyToOne
    @JoinColumn(name = "backgroundId")
    var background: BackgroundFile? = null

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    val created: Date? = Date()

    override fun getId(): UUID {
        return id!!
    }

    constructor(playablePuzzleDTO: PlayablePuzzleDTO) : this(playablePuzzleDTO.id, playablePuzzleDTO.name,
            playablePuzzleDTO.releaseDate) {
        if (playablePuzzleDTO.generatedTemplateId != null) {
            this.generatedTemplate = GeneratedTemplate(playablePuzzleDTO.generatedTemplateId)
        }
        if (playablePuzzleDTO.backgroundId != null) {
            this.background = BackgroundFile(playablePuzzleDTO.backgroundId)
        }
    }

    companion object {
        const val RESOURCE_NAME = "playablePuzzles"
    }
}

data class PlayablePuzzleDTO(val id: UUID,
                             val name: String? = null,
                             val generatedTemplateId: UUID? = null,
                             val backgroundId: UUID? = null,
                             val created: Date? = null,
                             val releaseDate: Date? = null,
                             val links: Map<String, String>? = null)
