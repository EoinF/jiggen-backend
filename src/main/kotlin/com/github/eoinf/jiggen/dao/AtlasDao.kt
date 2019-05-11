package com.github.eoinf.jiggen.dao

import com.github.eoinf.jiggen.config.JiggenConfig
import com.github.eoinf.jiggen.data.AtlasFile
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Paths
import java.util.UUID

interface IAtlasDao {
    fun get(id: UUID?): AtlasFile?
}

@Service
class AtlasDao(private val config: JiggenConfig) : IAtlasDao {


    override fun get(id: UUID?): AtlasFile? {
        val pathname = "${config.atlasFolder}/$id.atlas"
        return if (Files.exists(Paths.get(pathname))) {
            AtlasFile(id, pathname)
        } else {
            null
        }
    }
}