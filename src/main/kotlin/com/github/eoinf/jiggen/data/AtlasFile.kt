package com.github.eoinf.jiggen.data

import java.io.InputStream
import java.util.UUID


class AtlasFile(private val id: UUID?, val s3File: S3File): EntityWithId {
    override fun getId(): UUID {
        return id!!
    }

    fun inputStream() : InputStream {
        return s3File.inputStream
    }

    companion object {
        const val RESOURCE_NAME = "atlases"
    }
}