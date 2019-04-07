package com.github.eoinf.jiggen

import org.springframework.stereotype.Service
import java.awt.Color
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.File
import java.io.InputStream
import javax.imageio.IIOImage
import javax.imageio.ImageIO
import javax.imageio.ImageWriteParam

@Service
class ImageCompressor {

    private fun resizeImage(inputStream: InputStream, newSizePx: Int, maintainAspectRatio: Boolean = true): BufferedImage {
        val bufferedImage = ImageIO.read(inputStream)

        var width = newSizePx
        val height = newSizePx

        if (maintainAspectRatio) {
            val aspectRatio = bufferedImage.width / bufferedImage.height.toFloat()
            width = (width * aspectRatio).toInt()
        }

        val scaledImage = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)

        val tGraphics2D = scaledImage.createGraphics()
        tGraphics2D.background = Color.WHITE
        tGraphics2D.paint = Color.WHITE
        tGraphics2D.fillRect(0, 0, width, height)
        tGraphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC)
        tGraphics2D.drawImage(bufferedImage, 0, 0, width, height, null)

        return scaledImage
    }

    private fun saveCompressedImage(destination: String, bufferedImage: BufferedImage) {
        val file = File(destination)

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

    fun compressAndSave(destination: String, file: File) {
        file.inputStream().use {
            saveCompressedImage(destination, resizeImage(it, 800))
        }
    }

    fun createThumbnailAndSave(destination: String, file: File, thumbnailSize: Int) {
        file.inputStream().use {
            val thumbnail = resizeImage(it, thumbnailSize, false)
            ImageIO.write(thumbnail, "jpg", File(destination))
        }
    }
}
