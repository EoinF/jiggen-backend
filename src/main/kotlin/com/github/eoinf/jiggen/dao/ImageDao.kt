package com.github.eoinf.jiggen.dao

import com.github.eoinf.jiggen.config.JiggenConfig
import com.github.eoinf.jiggen.data.BackgroundFileDTO
import com.github.eoinf.jiggen.data.ImageFile
import com.github.eoinf.jiggen.data.TemplateFileDTO
import com.github.eoinf.jiggen.exception.NoMatchingResourceEntryException
import com.github.eoinf.jiggen.tasks.GeneratedTaskRunner
import org.springframework.stereotype.Service
import spark.Request
import java.awt.Color
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import javax.imageio.IIOImage
import javax.imageio.ImageIO
import javax.imageio.ImageWriteParam


interface IImageDao {
    fun get(id: String?, extension: String?): ImageFile?
    fun save(request: Request?, id: UUID, extension: String, inputStream: InputStream)
}

@Service
class ImageDao(private val config: JiggenConfig, private val templateDao: ITemplateDao,
               private val backgroundDao: BackgroundDao,
               private val generatedTaskRunner: GeneratedTaskRunner) : IImageDao {


    override fun get(id: String?, extension: String?): ImageFile? {
        val pathname = "${config.imageFolder}/$id.$extension"
        return if (Files.exists(Paths.get(pathname))) {
            ImageFile(id, pathname)
        } else {
            null
        }
    }

    override fun save(request: Request?, id: UUID, extension: String, inputStream: InputStream) {
        val resource = hasMatchingResource(request, id)

        if (resource != null) {
            val file = saveImageToFile("${config.imageFolder}/$id.$extension", inputStream)

            // If it's a template, we can get the background worker to generate the template puzzle
            if (resource is TemplateFileDTO) {
                generatedTaskRunner.generateNewTemplate(id, file.absolutePath)
            } else if (resource is BackgroundFileDTO) {
                //saveThumbailImageToFile(file, id)
            }
        } else {
            throw NoMatchingResourceEntryException("Corresponding image entry does not exist")
        }
    }

    fun saveThumbailImageToFile(file: File, id: UUID) {
        file.inputStream().use {
            saveCompressedImage("${config.imageFolder}/$id-compressed.jpg", resizeImage(it))
        }
    }

    private fun hasMatchingResource(request: Request?, id: UUID): Any? {
        if (request == null) {
            return true
        } else {
            return templateDao.get(request, id) ?: backgroundDao.get(request, id)
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

    private fun resizeImage(inputStream: InputStream): BufferedImage {
        val bufferedImage = ImageIO.read(inputStream)
        val aspectRatio = bufferedImage.width / bufferedImage.height.toFloat()

        val width = (800 * aspectRatio).toInt()
        val height = 800
        val scaledImage = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)

        val tGraphics2D = scaledImage.createGraphics()
        tGraphics2D.background = Color.WHITE
        tGraphics2D.paint = Color.WHITE
        tGraphics2D.fillRect(0, 0, width, height)
        tGraphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        tGraphics2D.drawImage(bufferedImage, 0, 0, width, height, null)

        return scaledImage
    }

    private fun saveCompressedImage(filePath: String, bufferedImage: BufferedImage) {
        val file = File(filePath)

        val writer = ImageIO.getImageWritersByFormatName("jpg").next()
        ImageIO.createImageOutputStream(file).use { imageOutputStream ->
            writer.output = imageOutputStream
            val param = writer.defaultWriteParam
            param.compressionMode = ImageWriteParam.MODE_EXPLICIT
            param.compressionQuality = 0.2f

            writer.write(null, IIOImage(bufferedImage, null, null), param)
            imageOutputStream.flush()
        }
        writer.dispose()
    }
}