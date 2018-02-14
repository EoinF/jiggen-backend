package com.github.eoinf.jiggen.dao

import com.github.eoinf.jiggen.BackgroundRepository
import com.github.eoinf.jiggen.data.BackgroundFile
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

interface IBackgroundDao {
    fun get() : List<BackgroundFile>
    fun get(id: UUID?) : BackgroundFile?
    fun save(background: BackgroundFile) : BackgroundFile
}

class BackgroundRepoDao : IBackgroundDao {
    @Autowired lateinit var backgroundRepository: BackgroundRepository

    override fun get(id: UUID?) : BackgroundFile? {
        return backgroundRepository.findOne(id)
    }

    override fun get() : List<BackgroundFile> {
        return backgroundRepository.findAll().toList()
    }

    override fun save(background: BackgroundFile) : BackgroundFile {
        return backgroundRepository.save(background)
    }
}