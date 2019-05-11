package com.github.eoinf.jiggen.dao

import com.github.eoinf.jiggen.ImageCompressor
import com.github.eoinf.jiggen.S3BucketService
import com.github.eoinf.jiggen.config.JiggenConfig
import com.github.eoinf.jiggen.data.BackgroundFileDTO
import com.github.eoinf.jiggen.data.ImageFile
import com.github.eoinf.jiggen.data.S3ImageFile
import com.github.eoinf.jiggen.data.TemplateFileDTO
import com.github.eoinf.jiggen.exception.NoMatchingResourceEntryException
import com.github.eoinf.jiggen.tasks.GeneratedTaskRunner
import org.springframework.stereotype.Service
import spark.Request
import java.io.File
import java.io.InputStream
import java.util.UUID

@Service
class ImageDao(private val config: JiggenConfig, private val templateDao: ITemplateDao,
               private val backgroundDao: BackgroundDao,
               private val generatedTaskRunner: GeneratedTaskRunner,
               private val imageCompressor: ImageCompressor,
               private val s3BucketService: S3BucketService
) {


    fun get(id: String, extension: String?): S3ImageFile? {
        return S3ImageFile(s3BucketService.getFileStream("$id.$extension"))
    }

    fun save(request: Request, id: UUID, extension: String, inputStream: InputStream) {
        val resource = hasMatchingResource(request, id)

        if (resource != null) {
            val file = saveImageToFile("$id.$extension", inputStream)

            // If it's a template, we can get the background worker to generate the template puzzle
            if (resource is TemplateFileDTO) {
                generatedTaskRunner.generateNewTemplate(id, file.absolutePath)
            } else if (resource is BackgroundFileDTO) {
                saveCompressedImageToFile(file, id)
                saveThumbnailToFile(file, id)
                // Remove the local copy
                file.delete()
            }
        } else {
            throw NoMatchingResourceEntryException("Corresponding image entry does not exist")
        }
    }

    private fun saveImageToFile(destinationFileName: String, inputStream: InputStream): File {
        val destinationPath = "${config.imageFolder}/$destinationFileName"
        val destinationFile = File(destinationPath)
        destinationFile.parentFile.mkdirs()
        destinationFile.createNewFile()

        destinationFile.outputStream().use {
            inputStream.copyTo(it)
        }
        s3BucketService.putFile(destinationFileName, destinationPath)
        return destinationFile
    }

    fun saveCompressedImageToFile(file: File, id: UUID) {
        val fileName = "$id-compressed.jpg"
        val destinationPath = "${config.imageFolder}/$fileName"
        val tmpFile = imageCompressor.compressAndSave(destinationPath, file)
        s3BucketService.putFile(fileName, destinationPath)
        tmpFile.delete()
    }

    fun saveThumbnailToFile(file: File, id: UUID) {
        val fileName = "$id-thumbnail48x48.jpg"
        val destinationPath = "${config.imageFolder}/$fileName"
        file.inputStream().use {
            val tmpFile = imageCompressor.createThumbnailAndSave(destinationPath, file, 48)
            s3BucketService.putFile(fileName, destinationPath)
            tmpFile.delete()
        }

    }

    private fun hasMatchingResource(request: Request, id: UUID): Any? {
        return templateDao.get(request, id) ?: backgroundDao.get(request, id)
    }

    fun getFile(id: String, extension: String?): ImageFile? {
        val pathname = "${config.imageFolder}/$id.$extension"
        val imageFile = ImageFile(id, pathname)
        if (s3BucketService.downloadToFile("$id.$extension", pathname)) {
            return imageFile
        } else {
            return null
        }
    }
}