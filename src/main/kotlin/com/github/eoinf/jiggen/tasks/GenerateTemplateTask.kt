package com.github.eoinf.jiggen.tasks

import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.PixmapIO
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.tools.texturepacker.TexturePacker
import com.github.eoinf.jiggen.PuzzleExtractor.Decoder.DecodedTemplate
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzleFactory
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzleGraph
import com.github.eoinf.jiggen.dao.IGeneratedTemplateDao
import com.github.eoinf.jiggen.data.GeneratedTemplate
import com.github.eoinf.jiggen.data.TemplateFile
import org.apache.logging.log4j.LogManager
import java.io.File
import java.util.*

class GenerateTemplateTask(private val imageId: UUID, private val imageLocation: String,
                           private val imageFolder: String, private val atlasFolder: String,
                           private val generatedTemplateDao: IGeneratedTemplateDao) : Runnable {

    private val logger = LogManager.getLogger()


    override fun run() {
        try {
            logger.info("GeneratedTemplateTask::run Fetching file handle")

            val handle = FileHandle(imageLocation)
            val pixmap = Pixmap(handle)

            logger.info("GeneratedTemplateTask::run Decoding texture")
            val decodedTemplate = DecodedTemplate(pixmap)

            logger.info("GeneratedTemplateTask::run Generating puzzle")
            val puzzleGraph = PuzzleFactory.generatePixmapPuzzleFromTemplate(decodedTemplate)

            val puzzleFolderName = "$imageFolder/$imageId"

            logger.info("GeneratedTemplateTask::run Saving files")
            saveFilesToFolder(puzzleGraph, puzzleFolderName)


            logger.info("GeneratedTemplateTask::run Packing textures")
            val settings = TexturePacker.Settings()
            TexturePacker.process(settings, puzzleFolderName, atlasFolder, imageId.toString())

            logger.info("GeneratedTemplateTask::run Saving resource")
            val savedResource = generatedTemplateDao.save(GeneratedTemplate(imageId, TemplateFile(imageId)))

            savedResource ?:
                    throw Exception("Saved resource was null")

            logger.info("Saved generated template $imageId successfully")
        } catch(ex: Exception) {
            logger.info("GeneratedTemplateTask::run Exception handled")
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
}