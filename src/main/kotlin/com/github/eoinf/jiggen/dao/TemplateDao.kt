package com.github.eoinf.jiggen.dao

import com.github.eoinf.jiggen.TemplateRepository
import com.github.eoinf.jiggen.data.TemplateFile
import org.springframework.beans.factory.annotation.Autowired

interface ITemplateDao {
    fun get() : Array<TemplateFile>
    fun get(id: Int?) : TemplateFile?
    fun save(template: TemplateFile) : TemplateFile
}

class TemplateRepoDao : ITemplateDao {
    @Autowired lateinit var templateRepository: TemplateRepository

    override fun get(id: Int?) : TemplateFile? {
        return templateRepository.findOne(id)
    }

    override fun get() : Array<TemplateFile> {
        return templateRepository.findAll().toList().toTypedArray()
    }

    override fun save(template: TemplateFile) : TemplateFile {
        return templateRepository.save(template)
    }


}