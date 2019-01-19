package com.github.eoinf.jiggen.data

import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Temporal

@Entity
data class BackgroundFile(
        @Id
        @Column(columnDefinition = "BINARY(16)")
        private val id: UUID? = null,
        val name: String? = null,
        val extension: String? = null,
        @Temporal(javax.persistence.TemporalType.TIMESTAMP)
        val releaseDate: Date? = null
) : EntityWithId {

    override fun getId(): UUID {
        return id!!
    }

    companion object {
        const val RESOURCE_NAME = "backgrounds"
    }

    constructor(backgroundFileDTO: BackgroundFileDTO)
            : this(backgroundFileDTO.id, backgroundFileDTO.name, backgroundFileDTO.extension)
}

data class BackgroundFileDTO(val id: UUID? = null,
                             val name: String? = null,
                             val extension: String? = null,
                             val releaseDate: Date? = null,
                             val links: Map<String, String>? = null)