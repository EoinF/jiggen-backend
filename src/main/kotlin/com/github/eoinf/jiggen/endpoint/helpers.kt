package com.github.eoinf.jiggen.endpoint


import spark.Response

fun Response.setJsonContentType() {
    this.header("Content-Type", "application/json")
}

fun Response.setImageContentType(extension: String) {
    this.header("Content-Type", "image/$extension")
}

fun splitFilename(filename: String) : List<String> {
    return filename.split('.')
}