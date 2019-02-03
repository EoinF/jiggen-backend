package com.github.eoinf.jiggen.data

import java.io.File
import java.util.*

class ImageFile(private val id: String?, pathname: String): File(pathname), EntityWithId {
    override fun getId(): UUID {
        return UUID.fromString(id)
    }

    companion object {
        const val RESOURCE_NAME = "images"
    }
}