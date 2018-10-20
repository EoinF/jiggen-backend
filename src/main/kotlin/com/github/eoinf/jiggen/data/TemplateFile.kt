package com.github.eoinf.jiggen.data

import java.util.*
import javax.persistence.*

@Entity
data class TemplateFile(
        @Id
        @Column(columnDefinition = "BINARY(16)")
        private val id: UUID? = null,
        val name: String? = null,
        val extension: String? = null
) : EntityWithId {
    override fun getId(): UUID {
        return id!!
    }

    companion object {
        const val RESOURCE_NAME = "templates"
    }

    @OneToMany(mappedBy = "templateFile", cascade = [(CascadeType.ALL)])
    var puzzleTemplates: Set<GeneratedTemplate>? = null

    constructor(templateFileDTO: TemplateFileDTO)
            : this(templateFileDTO.id, templateFileDTO.name, templateFileDTO.extension)
}

data class TemplateFileDTO(val id: UUID? = null,
                           val name: String? = null,
                           val extension: String? = null,
                           val puzzleTemplates: Set<GeneratedTemplateDTO>? = null,
                           val links: Map<String, String>? = null)