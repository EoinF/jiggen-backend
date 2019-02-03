package com.github.eoinf.jiggen.tasks

import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.PixmapIO
import com.badlogic.gdx.tools.texturepacker.TexturePacker
import com.github.eoinf.jiggen.PuzzleExtractor.Decoder.DecodedTemplate
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.IntRectangle
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzleFactory
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzleGraphTemplate
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzlePieceTemplate
import com.github.eoinf.jiggen.dao.GeneratedTemplateDao
import com.github.eoinf.jiggen.data.GeneratedTemplateDTO
import org.apache.logging.log4j.LogManager
import java.io.File
import java.util.*
import kotlin.collections.HashMap

class GenerateTemplateTask(private val imageId: UUID, private val imageLocation: String,
                           private val imageFolder: String, private val atlasFolder: String,
                           private val puzzleTemplateDao: GeneratedTemplateDao) : Runnable {

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

            logger.info("PuzzleTemplateTask::run Extracting atlas details")
            moveAtlasImagesToImagesFolder(atlasFolderTempFolder, imageFolder)
            moveAtlasTextToAtlasFolder(atlasFolderTempFolder, atlasFolder, packedImageId)
            deleteTempFiles(atlasFolderTempFolder)
            deleteTempFiles(puzzleFolderName)

            val generatedTemplate = GeneratedTemplateDTO(
                    id = packedImageId,
                    width=puzzleGraph.width,
                    height=puzzleGraph.height,
                    edges=puzzleGraph.edges,
                    vertices=toVertexDefinitionMap(puzzleGraph.vertices),
                    templateFileId=imageId,
                    extension = "png"
            )

            logger.info("PuzzleTemplateTask::run Saving resource")
            val savedResource = puzzleTemplateDao.save(generatedTemplate)

            savedResource ?: throw Exception("Saved resource was null")

            logger.info("Saved generated template $imageId successfully")
        } catch (ex: Exception) {
            logger.info("PuzzleTemplateTask::run Exception handled")
            logger.error("Failed to save generated template $imageId", ex)
        }
    }

    /**
     * Save each puzzle piece to a file
     * So it can be picked up by the texture packer and packed into one image
     */
    private fun saveFilesToFolder(puzzleGraph: PuzzleGraphTemplate, folderName: String) {
        puzzleGraph.vertices.forEach {
            val file = FileHandle("$folderName/${it.key}.png")
            val data = it.value.data
            if (data is Pixmap) {
                PixmapIO.writePNG(file, data)
            } else {
                throw Exception("Expected to save pixmap type, but received ${data::class} instead")
            }
        }
    }

    /**
     * Moves the generated atlas image to the IMAGES folder so it can be fetched as a resource
     * from the IMAGES endpoint
     */
    private fun moveAtlasImagesToImagesFolder(srcFolder: String, dstFolder: String) {
        File(srcFolder).copyRecursively(File(dstFolder))
    }

    /**
     * Moves the generated atlas file to the ATLAS folder so it can be fetched as a resource
     * from the ATLAS endpoint
     */
    private fun moveAtlasTextToAtlasFolder(srcFolder: String, dstFolder: String, atlasName: UUID) {
        val srcFile = File("$srcFolder/$atlasName.atlas")

        srcFile.inputStream().use { input ->
            File("$dstFolder/$atlasName.atlas").outputStream().use { output ->
                input.copyTo(output)
            }
        }
    }

    private fun deleteTempFiles(tmpFolder: String) {
        if (tmpFolder == "") {
            throw IllegalArgumentException("Tried to delete the root folder!")
        } else {
            File(tmpFolder).deleteRecursively()
        }
    }

    private fun <T> toVertexDefinitionMap(vertices: Map<Int, PuzzlePieceTemplate<T>>): Map<Int, IntRectangle> {
        val result = HashMap<Int, IntRectangle>()
        vertices.forEach {
            result[it.key] = IntRectangle(it.value.x(), it.value.y(), it.value.width, it.value.height)
        }
        return result
    }
}