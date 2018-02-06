package com.github.eoinf.jiggen.dao

import java.io.*

interface IImageDao {
    fun get(id: String?, extension: String?) : File?
    fun save(id: String, extension: String, inputStream: InputStream)
}

class ImageDao(private val imageFolder: String, val templateDao: ITemplateDao,
               val backgroundDao: IBackgroundDao) : IImageDao {
    override fun get(id: String?, extension: String?) : File? {
        return try {
            File("$imageFolder/$id.$extension")
        } catch (ex: FileNotFoundException) {
            null
        }
    }
    override fun save(id: String, extension: String, inputStream: InputStream) {
        if (doesImageExist(id)) {
            val file = File("$imageFolder/$id.$extension")
            file.parentFile.mkdirs()
            file.createNewFile()
            inputStream.copyTo(file.outputStream())
        } else {
            throw Exception("Corresponding image entry does not exist")
        }
    }

    private fun doesImageExist(id: String): Boolean {
        return (templateDao.findByImageId(id) != null
                || backgroundDao.findByImageId(id) != null);
    }
}