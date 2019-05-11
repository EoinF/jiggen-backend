package com.github.eoinf.jiggen.tasks

import com.github.eoinf.jiggen.config.JiggenConfig
import com.github.eoinf.jiggen.dao.GeneratedTemplateDao
import org.springframework.stereotype.Service
import java.util.UUID
import java.util.concurrent.ExecutorService

@Service
class GeneratedTaskRunner(private val executorService: ExecutorService,
                          private val jiggenConfiguration: JiggenConfig,
                          private val puzzleTemplateDao: GeneratedTemplateDao) {

    fun generateNewTemplate(id: UUID, filePath: String) {
        executorService.submit(GenerateTemplateTask(
                id, filePath, jiggenConfiguration.imageFolder, jiggenConfiguration.atlasFolder, puzzleTemplateDao)
        )
    }
}