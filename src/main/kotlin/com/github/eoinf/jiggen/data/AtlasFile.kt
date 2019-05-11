package com.github.eoinf.jiggen.data

import java.io.File
import java.util.UUID

class AtlasFile(private val id: UUID?, pathname: String): File(pathname), EntityWithId {
    override fun getId(): UUID {
        return id!!
    }

    companion object {
        const val RESOURCE_NAME = "atlases"
    }
}