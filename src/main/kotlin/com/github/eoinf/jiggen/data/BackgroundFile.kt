package com.github.eoinf.jiggen.data

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class BackgroundFile(
        @Id private val id: UUID? = null,
        val name: String? = null,
        val extension: String? = null
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
                             val links: Map<String, String>)