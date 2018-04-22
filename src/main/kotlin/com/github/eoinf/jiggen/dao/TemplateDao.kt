package com.github.eoinf.jiggen.dao

import com.github.eoinf.jiggen.DataMapper
import com.github.eoinf.jiggen.TemplateRepository
import com.github.eoinf.jiggen.data.TemplateFile
import com.github.eoinf.jiggen.data.TemplateFileDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

interface ITemplateDao {
    fun get() : List<TemplateFileDTO>
    fun get(id: UUID) : TemplateFileDTO?
    fun save(template: TemplateFileDTO) : TemplateFileDTO
}

@Service
open class TemplateRepoDao(private val dataMapper: DataMapper) : ITemplateDao {
    @Autowired lateinit var templateRepository: TemplateRepository

    @Transactional
    override fun get(id: UUID) : TemplateFileDTO? {
        val templateFile = templateRepository.findById(id).orElse(null)
        if (templateFile == null)
            return null
        else
            return dataMapper.toTemplateFileDTO(templateFile, depth = 2)
    }

    override fun get() : List<TemplateFileDTO> {
        return templateRepository.findAll().toList().map {
            dataMapper.toTemplateFileDTO(it)
        }
    }

    override fun save(template: TemplateFileDTO) : TemplateFileDTO {
        return dataMapper.toTemplateFileDTO(templateRepository.save(TemplateFile(template)))
    }
}