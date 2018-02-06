package com.github.eoinf.jiggen.dao

import com.github.eoinf.jiggen.BackgroundRepository
import com.github.eoinf.jiggen.data.BackgroundFile
import org.springframework.beans.factory.annotation.Autowired

interface IBackgroundDao {
    fun get() : Array<BackgroundFile>
    fun get(id: Int?) : BackgroundFile?
    fun save(background: BackgroundFile) : BackgroundFile
    fun findByImageId(id: String): BackgroundFile?
}

class BackgroundRepoDao : IBackgroundDao {
    @Autowired lateinit var backgroundRepository: BackgroundRepository

    override fun get(id: Int?) : BackgroundFile? {
        return backgroundRepository.findOne(id)
    }

    override fun get() : Array<BackgroundFile> {
        return backgroundRepository.findAll().toList().toTypedArray()
    }

    override fun save(background: BackgroundFile) : BackgroundFile {
        return backgroundRepository.save(background)
    }

    override fun findByImageId(id: String): BackgroundFile? {
        return backgroundRepository.findByImageId(id)
    }
}