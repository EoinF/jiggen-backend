package com.github.eoinf.jiggen.tasks

import com.github.eoinf.jiggen.dao.IPuzzleTemplateDao
import java.io.File
import java.util.*
import java.util.concurrent.ExecutorService

class GeneratedTaskRunner(private val executorService: ExecutorService, private val imageFolder: String,
                          private val atlasFolder: String, private val puzzleTemplateDao: IPuzzleTemplateDao) {

    fun generateNewTemplate(id: UUID, filePath: String) {
        executorService.submit(GenerateTemplateTask(id, filePath, imageFolder, atlasFolder, puzzleTemplateDao))
    }
}