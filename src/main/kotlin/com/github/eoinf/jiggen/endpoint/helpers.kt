package com.github.eoinf.jiggen.endpoint


import com.github.eoinf.jiggen.JsonTransformer
import spark.Response

fun Response.setJsonContentType() {
    this.header("Content-Type", "application/json")
}

fun Response.setPlainTextContentType() {
    this.header("Content-Type", "text/plain")
}

fun Response.setImageContentType(extension: String) {
    this.header("Content-Type", "image/$extension")
}

fun Response.setGzipEncoding() {
    this.header("Content-Encoding", "gzip")
}

fun Response.setupJsonResponse(body: Any, jsonTransformer: JsonTransformer): String {
    this.setJsonContentType()
    val jsonString = jsonTransformer.toJson(body)
    this.header("Content-Length", jsonString.length.toString())
    return jsonString
}