package com.github.eoinf.jiggen.endpoint

import com.github.eoinf.jiggen.JsonTransformer
import com.github.eoinf.jiggen.dao.IBackgroundDao
import com.github.eoinf.jiggen.data.BackgroundFile
import org.apache.logging.log4j.LogManager
import spark.Spark.get
import spark.Spark.path

private val logger = LogManager.getLogger()

fun backgroundsEndpoint(backgroundDao: IBackgroundDao, jsonTransformer: JsonTransformer) {
    path("/backgrounds") {
        get("") { req, res ->
            logger.info("GET All request handled")
            setJsonContentType(res)
            jsonTransformer.toJson(backgroundDao.get())
        }
        get("/:id") { req, res ->
            logger.info("GET request handled {}", req.params(":id"))
            val id = req.params(":id").toIntOrNull()

            val background: BackgroundFile? = backgroundDao.get(id)

            if (background == null) {
                res.status(404)
                ""
            } else {
                setJsonContentType(res)
                jsonTransformer.toJson(background)
            }
        }
    }

}