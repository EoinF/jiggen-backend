package com.github.eoinf.jiggen.dao

import com.github.eoinf.jiggen.DataMapper
import com.github.eoinf.jiggen.TemplateRepository
import com.github.eoinf.jiggen.data.TemplateFile
import com.github.eoinf.jiggen.data.TemplateFileDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import spark.Request
import java.util.*

interface ITemplateDao {
    fun get(request: Request): List<TemplateFileDTO>
    fun get(request: Request, id: UUID): TemplateFileDTO?
    fun save(request: Request, template: TemplateFileDTO): TemplateFileDTO
}

@Service
open class TemplateRepoDao(private val dataMapper: DataMapper) : ITemplateDao {
    @Autowired
    lateinit var templateRepository: TemplateRepository

    @Transactional
    override fun get(request: Request, id: UUID): TemplateFileDTO? {
        val templateFile = templateRepository.findById(id).orElse(null)
        if (templateFile == null)
            return null
        else
            return dataMapper.toTemplateFileDTO(request, templateFile, false)
    }

    override fun get(request: Request): List<TemplateFileDTO> {
        return templateRepository.findAll().toList().map {
            dataMapper.toTemplateFileDTO(request, it, true)
        }
    }

    override fun save(request: Request, template: TemplateFileDTO): TemplateFileDTO {
        return dataMapper.toTemplateFileDTO(request, templateRepository.save(TemplateFile(template)), false)
    }
}