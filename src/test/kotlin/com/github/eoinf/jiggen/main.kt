package com.github.eoinf.jiggen

/*
    Entry point for running the app locally for dev testing
 */
fun main(args: Array<String>) {
    val app = Application(TestPuzzleDao(), TestTemplateDao(), TestBackgroundDao(), JsonTransformer())
    app.init()
}