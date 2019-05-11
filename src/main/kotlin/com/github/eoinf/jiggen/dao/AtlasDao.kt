package com.github.eoinf.jiggen.dao

import com.github.eoinf.jiggen.S3BucketService
import com.github.eoinf.jiggen.data.AtlasFile
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AtlasDao(private val s3BucketService: S3BucketService) {

    fun get(id: UUID?): AtlasFile? {
        val pathname = "$id.atlas"
        return AtlasFile(id, s3BucketService.getFileStream(pathname))
    }
}