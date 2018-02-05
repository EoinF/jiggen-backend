package com.github.eoinf.jiggen

import com.github.eoinf.jiggen.dao.ImageDao

/*
    Entry point for running the app locally for dev testing
 */
fun main(args: Array<String>) {
    val imagesFolder = "~/personal/jiggen-images"
    val app = Application(TestPuzzleDao(), TestTemplateDao(), TestBackgroundDao(), ImageDao(imagesFolder), JsonTransformer())
    app.baseUrl = "localhost"
    app.init()
}