package com.github.eoinf.jiggen.dao

import com.github.eoinf.jiggen.ImageCompressor
import com.github.eoinf.jiggen.config.JiggenConfig
import com.github.eoinf.jiggen.data.BackgroundFileDTO
import com.github.eoinf.jiggen.data.ImageFile
import com.github.eoinf.jiggen.data.TemplateFileDTO
import com.github.eoinf.jiggen.exception.NoMatchingResourceEntryException
import com.github.eoinf.jiggen.tasks.GeneratedTaskRunner
import org.springframework.stereotype.Service
import spark.Request
import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.util.UUID

@Service
class ImageDao(private val config: JiggenConfig, private val templateDao: ITemplateDao,
               private val backgroundDao: BackgroundDao,
               private val generatedTaskRunner: GeneratedTaskRunner,
               private val imageCompressor: ImageCompressor) {


    fun get(id: String?, extension: String?): ImageFile? {
        val pathname = "${config.imageFolder}/$id.$extension"
        return if (Files.exists(Paths.get(pathname))) {
            ImageFile(id, pathname)
        } else {
            null
        }
    }

    fun save(request: Request?, id: UUID, extension: String, inputStream: InputStream) {
        val resource = hasMatchingResource(request, id)

        if (resource != null) {
            val file = saveImageToFile("${config.imageFolder}/$id.$extension", inputStream)

            // If it's a template, we can get the background worker to generate the template puzzle
            if (resource is TemplateFileDTO) {
                generatedTaskRunner.generateNewTemplate(id, file.absolutePath)
            } else if (resource is BackgroundFileDTO) {
                saveCompressedImageToFile(file, id)
                saveThumbnailToFile(file, id)
            }
        } else {
            throw NoMatchingResourceEntryException("Corresponding image entry does not exist")
        }
    }

    private fun saveImageToFile(filePath: String, inputStream: InputStream): File {
        val file = File(filePath)
        file.parentFile.mkdirs()
        file.createNewFile()

        file.outputStream().use {
            inputStream.copyTo(it)
        }
        return file
    }

    fun saveCompressedImageToFile(file: File, id: UUID) {
        imageCompressor.compressAndSave("${config.imageFolder}/$id-compressed.jpg", file)
    }

    fun saveThumbnailToFile(file: File, id: UUID) {
        file.inputStream().use {
            imageCompressor.createThumbnailAndSave("${config.imageFolder}/$id-thumbnail48x48.jpg", file, 48)
        }
    }

    private fun hasMatchingResource(request: Request?, id: UUID): Any? {
        if (request == null) {
            return true
        } else {
            return templateDao.get(request, id) ?: backgroundDao.get(request, id)
        }
    }

}