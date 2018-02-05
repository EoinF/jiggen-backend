package com.github.eoinf.jiggen.dao

import java.io.*

interface IImageDao {
    fun get(id: String?) : File?
    fun save(id: String, inputStream: InputStream)
}

class ImageDao(private val imageFolder: String): IImageDao {
    override fun get(id: String?) : File? {
        return try {
            File(imageFolder + id)
        } catch (ex: FileNotFoundException) {
            null
        }
    }
    override fun save(id: String, inputStream: InputStream) {
        val file = File(imageFolder + "/" + id)
        file.createNewFile()
        inputStream.copyTo(file.outputStream())
    }
}