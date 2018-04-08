package com.github.eoinf.jiggen.tasks

import com.github.eoinf.jiggen.JiggenConfiguration
import com.github.eoinf.jiggen.dao.PuzzleTemplateDao
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.ExecutorService

@Service
class GeneratedTaskRunner(private val executorService: ExecutorService,
                          private val jiggenConfiguration: JiggenConfiguration,
                          private val puzzleTemplateDao: PuzzleTemplateDao) {

    fun generateNewTemplate(id: UUID, filePath: String) {
        executorService.submit(GenerateTemplateTask(
                id, filePath, jiggenConfiguration.imageFolder, jiggenConfiguration.atlasFolder, puzzleTemplateDao)
        )
    }
}