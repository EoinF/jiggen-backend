package com.github.eoinf.jiggen.dao

import com.github.eoinf.jiggen.TemplateRepository
import com.github.eoinf.jiggen.data.TemplateFile
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

interface ITemplateDao {
    fun get() : List<TemplateFile>
    fun get(id: UUID?) : TemplateFile?
    fun save(template: TemplateFile) : TemplateFile
}

class TemplateRepoDao : ITemplateDao {
    @Autowired lateinit var templateRepository: TemplateRepository

    override fun get(id: UUID?) : TemplateFile? {
        return templateRepository.findOne(id)
    }

    override fun get() : List<TemplateFile> {
        return templateRepository.findAll().toList()
    }

    override fun save(template: TemplateFile) : TemplateFile {
        return templateRepository.save(template)
    }
}