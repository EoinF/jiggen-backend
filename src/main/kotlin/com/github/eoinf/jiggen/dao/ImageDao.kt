package com.github.eoinf.jiggen.dao

import com.github.eoinf.jiggen.config.JiggenConfig
import com.github.eoinf.jiggen.data.ImageFile
import com.github.eoinf.jiggen.data.TemplateFileDTO
import com.github.eoinf.jiggen.exception.NoMatchingResourceEntryException
import com.github.eoinf.jiggen.tasks.GeneratedTaskRunner
import org.springframework.stereotype.Service
import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

interface IImageDao {
    fun get(id: UUID?, extension: String?): ImageFile?
    fun save(id: UUID, extension: String, inputStream: InputStream)
}

@Service
class ImageDao(private val config: JiggenConfig, private val templateDao: ITemplateDao,
               private val backgroundDao: IBackgroundDao,
               private val generatedTaskRunner: GeneratedTaskRunner) : IImageDao {


    override fun get(id: UUID?, extension: String?): ImageFile? {
        val pathname = "${config.imageFolder}/$id.$extension"
        return if (Files.exists(Paths.get(pathname))) {
            ImageFile(id, pathname)
        } else {
            null
        }
    }

    override fun save(id: UUID, extension: String, inputStream: InputStream) {
        val resource = hasMatchingResource(id)

        if (resource != null) {
            val file = File("${config.imageFolder}/$id.$extension")
            file.parentFile.mkdirs()
            file.createNewFile()

            file.outputStream().use {
                inputStream.copyTo(it)
            }

            // If it's a template, we can get the background worker to generate the template puzzle
            if (resource is TemplateFileDTO) {
                generatedTaskRunner.generateNewTemplate(id, file.absolutePath)
            }
        } else {
            throw NoMatchingResourceEntryException("Corresponding image entry does not exist")
        }
    }

    private fun hasMatchingResource(id: UUID): Any? {
        return templateDao.get(id) ?: backgroundDao.get(id)
    }
}