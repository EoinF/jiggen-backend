package com.github.eoinf.jiggen.dao

import com.github.eoinf.jiggen.BackgroundRepository
import com.github.eoinf.jiggen.DataMapper
import com.github.eoinf.jiggen.data.BackgroundFile
import com.github.eoinf.jiggen.data.BackgroundFileDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

interface IBackgroundDao {
    fun get(): List<BackgroundFileDTO>
    fun get(id: UUID): BackgroundFileDTO?
    fun save(background: BackgroundFileDTO): BackgroundFileDTO
}

@Service
class BackgroundRepoDao(private val dataMapper: DataMapper) : IBackgroundDao {
    @Autowired
    lateinit var backgroundRepository: BackgroundRepository

    override fun get(id: UUID): BackgroundFileDTO? {
        val backgroundFile = backgroundRepository.findById(id).orElse(null)
        if (backgroundFile == null)
            return null
        else
            return dataMapper.toBackgroundFileDTO(backgroundFile, false)
    }

    override fun get(): List<BackgroundFileDTO> {
        return backgroundRepository.findAllByReleaseDateBefore(Date(Instant.now().toEpochMilli())).map {
            dataMapper.toBackgroundFileDTO(it, true)
        }
    }

    override fun save(background: BackgroundFileDTO): BackgroundFileDTO {
        return dataMapper.toBackgroundFileDTO(backgroundRepository.save(BackgroundFile(background)), false)
    }
}