package com.github.eoinf.jiggen.dao

import com.github.eoinf.jiggen.BackgroundRepository
import com.github.eoinf.jiggen.DataMapper
import com.github.eoinf.jiggen.data.BackgroundFile
import com.github.eoinf.jiggen.data.BackgroundFileDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import spark.Request
import java.time.Instant
import java.util.*

@Service
class BackgroundDao(private val dataMapper: DataMapper) {
    @Autowired
    lateinit var backgroundRepository: BackgroundRepository

    fun get(request: Request, id: UUID): BackgroundFileDTO? {
        val backgroundFile = backgroundRepository.findById(id).orElse(null)
        if (backgroundFile == null)
            return null
        else
            return dataMapper.toBackgroundFileDTO(request, backgroundFile, false)
    }

    fun get(request: Request): List<BackgroundFileDTO> {
        return backgroundRepository.findAllByReleaseDateBefore(Date(Instant.now().toEpochMilli())).map {
            dataMapper.toBackgroundFileDTO(request, it, true)
        }
    }

    fun getAll(request: Request): List<BackgroundFileDTO> {
        return backgroundRepository.findAll().map {
            dataMapper.toBackgroundFileDTO(request, it, true)
        }
    }

    fun save(request: Request, background: BackgroundFileDTO): BackgroundFileDTO {
        return dataMapper.toBackgroundFileDTO(request, backgroundRepository.save(BackgroundFile(background)), false)
    }
}