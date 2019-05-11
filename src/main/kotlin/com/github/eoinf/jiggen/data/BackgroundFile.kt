package com.github.eoinf.jiggen.data

import java.util.Date
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Temporal

@Entity
class BackgroundFile(
        @Id
        @Column(columnDefinition = "BINARY(16)")
        private val id: UUID? = null,
        val name: String? = null,
        val extension: String? = null,
        val tags: Array<String>? = null,
        val author: String? = null,
        @Temporal(javax.persistence.TemporalType.TIMESTAMP)
        var releaseDate: Date? = null
) : EntityWithId {

    override fun getId(): UUID {
        return id!!
    }

    companion object {
        const val RESOURCE_NAME = "backgrounds"
    }

    constructor(backgroundFileDTO: BackgroundFileDTO)
            : this(backgroundFileDTO.id, backgroundFileDTO.name, backgroundFileDTO.extension, backgroundFileDTO.tags, backgroundFileDTO.author, backgroundFileDTO.releaseDate) {
        if (this.releaseDate == null) {
            this.releaseDate = Date()
        }
    }
}

data class BackgroundFileDTO(val id: UUID? = null,
                             val name: String? = null,
                             val extension: String? = null,
                             val releaseDate: Date? = null,
                             val tags: Array<String>? = null,
                             val author: String? = null,
                             val links: Map<String, String>? = null) {
    override fun equals(other: Any?) = this.id == (other as BackgroundFileDTO).id
    override fun hashCode() = id?.hashCode() ?: 0
}