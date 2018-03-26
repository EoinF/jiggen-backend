package com.github.eoinf.jiggen.tasks

import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.PixmapIO
import com.badlogic.gdx.tools.texturepacker.TexturePacker
import com.github.eoinf.jiggen.PuzzleExtractor.Decoder.DecodedTemplate
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzleFactory
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzleGraph
import com.github.eoinf.jiggen.dao.IPuzzleTemplateDao
import com.github.eoinf.jiggen.data.PuzzleTemplate
import com.github.eoinf.jiggen.data.TemplateFile
import org.apache.logging.log4j.LogManager
import java.io.File
import java.util.*

class GenerateTemplateTask(private val imageId: UUID, private val imageLocation: String,
                           private val imageFolder: String, private val atlasFolder: String,
                           private val puzzleTemplateDao: IPuzzleTemplateDao) : Runnable {

    private val logger = LogManager.getLogger()


    override fun run() {
        try {
            logger.info("PuzzleTemplateTask::run Fetching file handle")

            val handle = FileHandle(imageLocation)
            val pixmap = Pixmap(handle)

            logger.info("PuzzleTemplateTask::run Decoding texture")
            val decodedTemplate = DecodedTemplate(pixmap)

            logger.info("PuzzleTemplateTask::run Generating puzzle")
            val puzzleGraph = PuzzleFactory.generatePixmapPuzzleFromTemplate(decodedTemplate)

            val puzzleFolderName = "$imageFolder/$imageId"
            val atlasFolderTempFolder = "$atlasFolder/$imageId"

            val packedImageId = UUID.randomUUID()

            logger.info("PuzzleTemplateTask::run Saving files")
            saveFilesToFolder(puzzleGraph, puzzleFolderName)

            logger.info("PuzzleTemplateTask::run Packing textures")
            val settings = TexturePacker.Settings()
            TexturePacker.process(settings, puzzleFolderName, atlasFolderTempFolder, packedImageId.toString())

            val puzzleTemplate = PuzzleTemplate(packedImageId, TemplateFile(imageId))
            logger.info("PuzzleTemplateTask::run Extracting atlas details")

            puzzleTemplate.atlasDetails = getAtlasFromFolder(atlasFolderTempFolder, packedImageId.toString())
            moveAtlasImageToImagesFolder(atlasFolderTempFolder, imageFolder, packedImageId)
            deleteTempFiles(atlasFolderTempFolder)
            deleteTempFiles(puzzleFolderName)

            logger.info("PuzzleTemplateTask::run Saving resource")
            val savedResource = puzzleTemplateDao.save(puzzleTemplate)

            savedResource ?:
                    throw Exception("Saved resource was null")

            logger.info("Saved generated template $imageId successfully")
        } catch(ex: Exception) {
            logger.info("PuzzleTemplateTask::run Exception handled")
            logger.error("Failed to save generated template $imageId", ex)
        }
    }

    /**
     * Save each puzzle piece to a file
     * So it can be picked up by the texture packer and packed into one image
     */
    private fun saveFilesToFolder(puzzleGraph: PuzzleGraph, folderName: String) {
        for (i in 0 until puzzleGraph.vertices.size) {
            val file = FileHandle("$folderName/$i.png")
            val data = puzzleGraph.vertices[i].data
            if (data is Pixmap) {
                PixmapIO.writePNG(file, data)
            } else {
                throw Exception("Expected to save pixmap type, but received ${data::class} instead")
            }
        }
    }

    /**
     * Gets the atlas details as a string from the atlas file
     */
    private fun getAtlasFromFolder(folderName: String, atlasName: String): String {
        val file = File("$folderName/$atlasName.atlas")
        return file.readText()
    }

    /**
     * Moves the generated atlas image to the images folder so it can be fetched as a resource
     * from the images endpoint
     */
    private fun moveAtlasImageToImagesFolder(srcFolder: String, dstFolder: String, packedImageId: UUID) {
        val srcFile = File("$srcFolder/$packedImageId.png")
        val dstFile = File("$dstFolder/$packedImageId.png").outputStream()

        srcFile.inputStream()
                .copyTo(dstFile)
    }

    private fun deleteTempFiles(tmpFolder: String) {
        if (tmpFolder == "") {
            throw IllegalArgumentException("Tried to delete the root folder!")
        } else {
            File(tmpFolder).deleteRecursively()
        }
    }
}