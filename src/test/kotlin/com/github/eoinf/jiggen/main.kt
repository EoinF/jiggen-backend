package com.github.eoinf.jiggen

import com.github.eoinf.jiggen.dao.ImageDao
import com.github.eoinf.jiggen.tasks.GeneratedTaskRunner
import java.util.concurrent.Executors

/*
    Entry point for running the app locally for dev testing
 */
fun main(args: Array<String>) {
    val imagesFolder: String
    val atlasFolder: String

    if (System.getProperty("os.name").contains("Windows")) {
        imagesFolder = System.getenv("APPDATA") + "\\jiggen\\images"
        atlasFolder = System.getenv("APPDATA") + "\\jiggen\\atlas"
    } else {
        imagesFolder = "/var/lib/jiggen/images"
        atlasFolder = "/var/lib/jiggen/atlas"
    }
    val templateDao = TestTemplateDao()
    val backgroundDao = TestBackgroundDao()
    val executorService = Executors.newSingleThreadExecutor()
    val puzzleTemplateDao = TestPuzzleTemplateDao()

    val taskRunner = GeneratedTaskRunner(executorService, imagesFolder, atlasFolder, puzzleTemplateDao)

    val app = Application(TestPuzzleDao(), templateDao, backgroundDao,
            ImageDao(imagesFolder, templateDao, backgroundDao, taskRunner), puzzleTemplateDao, JsonTransformer())
    app.baseUrl = "http://localhost:4567"
    app.init()
}