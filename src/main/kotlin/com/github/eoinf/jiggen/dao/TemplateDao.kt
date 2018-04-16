package com.github.eoinf.jiggen.dao

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
open class TemplateRepoDao : ITemplateDao {
    @Autowired lateinit var templateRepository: TemplateRepository

    @Transactional
    override fun get(id: UUID) : TemplateFileDTO? {
        val templateFile = templateRepository.findById(id).orElse(null)
        if (templateFile == null)
            return null
        else
            return TemplateFileDTO(templateFile, depth=1)
    }

    override fun get() : List<TemplateFileDTO> {
        return templateRepository.findAll().toList().map {
            TemplateFileDTO(it)
        }
    }

    override fun save(template: TemplateFileDTO) : TemplateFileDTO {
        return TemplateFileDTO(templateRepository.save(TemplateFile(template)))
    }
}