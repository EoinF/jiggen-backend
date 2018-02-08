package com.github.eoinf.jiggen

import com.github.eoinf.jiggen.dao.ImageDao
import com.github.eoinf.jiggen.tasks.GeneratedTaskRunner
import java.util.concurrent.Executors

/*
    Entry point for running the app locally for dev testing
 */
fun main(args: Array<String>) {
    val imagesFolder = "/var/lib/jiggen/images"
    val atlasFolder = "/var/lib/jiggen/atlas"
    val templateDao = TestTemplateDao()
    val backgroundDao = TestBackgroundDao()
    val executorService = Executors.newSingleThreadExecutor()
    val generatedTemplateDao = TestGeneratedTemplateDao()

    val taskRunner = GeneratedTaskRunner(executorService, imagesFolder, atlasFolder, generatedTemplateDao)

    val app = Application(TestPuzzleDao(), templateDao, backgroundDao,
            ImageDao(imagesFolder, templateDao, backgroundDao, taskRunner), JsonTransformer())
    app.baseUrl = "http://localhost:4567"
    app.init()
}