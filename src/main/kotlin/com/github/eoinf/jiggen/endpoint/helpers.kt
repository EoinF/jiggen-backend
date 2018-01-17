package com.github.eoinf.jiggen.endpoint


import spark.Response

fun setJsonContent(res: Response) {
    res.header("Content-Type", "application/json")
}