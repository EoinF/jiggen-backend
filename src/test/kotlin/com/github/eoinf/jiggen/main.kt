package com.github.eoinf.jiggen

import com.github.eoinf.jiggen.dao.ImageDao
import java.util.concurrent.Executors

/*
    Entry point for running the app locally for dev testing
 */
fun main(args: Array<String>) {
    val imagesFolder = "/var/lib/jiggen/images"
    val templateDao = TestTemplateDao()
    val backgroundDao = TestBackgroundDao()

    val app = Application(TestPuzzleDao(), templateDao, backgroundDao,
            ImageDao(imagesFolder, templateDao, backgroundDao), JsonTransformer())
    app.baseUrl = "http://localhost:4567"
    app.init()
}