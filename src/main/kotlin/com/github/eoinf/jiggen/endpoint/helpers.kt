package com.github.eoinf.jiggen.endpoint


import spark.Response

fun setJsonContentType(res: Response) {
    res.header("Content-Type", "application/json")
}

fun setPNGContentType(res: Response) {
    res.header("Content-Type", "image/png")
}