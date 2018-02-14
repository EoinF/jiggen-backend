package com.github.eoinf.jiggen.dao

import com.github.eoinf.jiggen.data.TemplateFile
import com.github.eoinf.jiggen.tasks.GeneratedTaskRunner
import java.io.*
import java.util.*

interface IImageDao {
    fun get(id: UUID?, extension: String?) : File?
    fun save(id: UUID, extension: String, inputStream: InputStream)
}

class ImageDao(private val imageFolder: String, private val templateDao: ITemplateDao,
               private val backgroundDao: IBackgroundDao,
               private val generatedTaskRunner: GeneratedTaskRunner) : IImageDao {
    override fun get(id: UUID?, extension: String?) : File? {
        return try {
            File("$imageFolder/$id.$extension")
        } catch (ex: FileNotFoundException) {
            null
        }
    }
    override fun save(id: UUID, extension: String, inputStream: InputStream) {
        val resource = hasMatchingResource(id)

        if (resource != null) {
            val file = File("$imageFolder/$id.$extension")
            file.parentFile.mkdirs()
            file.createNewFile()
            inputStream.copyTo(file.outputStream())

            // If it's a template, we can get the background worker to generate the template puzzle
            if (resource is TemplateFile) {
                generatedTaskRunner.generateNewTemplate(id, file.absolutePath)
            }
        }
        else {
            throw Exception("Corresponding image entry does not exist")
        }
    }

    private fun hasMatchingResource(id: UUID): Any? {
        return templateDao.get(id) ?: backgroundDao.get(id)
    }
}