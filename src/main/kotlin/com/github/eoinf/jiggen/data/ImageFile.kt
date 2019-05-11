package com.github.eoinf.jiggen.data

import java.io.File
import java.io.InputStream
import java.util.UUID

data class S3File(val inputStream: InputStream, val length: Long)

class S3ImageFile(private val s3File: S3File) {
    fun length(): Long {
        return s3File.length
    }

    fun inputStream(): InputStream {
        return s3File.inputStream
    }
}

class ImageFile(private val id: String?, pathname: String): EntityWithId {
    val file = File(pathname)
    override fun getId(): UUID {
        return UUID.fromString(id)
    }

    companion object {
        const val RESOURCE_NAME = "images"
    }

}