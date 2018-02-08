package com.github.eoinf.jiggen.dao

import com.github.eoinf.jiggen.GeneratedTemplateRepository
import com.github.eoinf.jiggen.data.GeneratedTemplate
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

interface IGeneratedTemplateDao {
    fun get(id: UUID?) : GeneratedTemplate?
    fun save(generatedTemplate: GeneratedTemplate) : GeneratedTemplate?
}

class GeneratedTemplateDao : IGeneratedTemplateDao {
    @Autowired lateinit var generatedTemplatedRepository: GeneratedTemplateRepository

    override fun get(id: UUID?): GeneratedTemplate? {
        return generatedTemplatedRepository.findOne(id)
    }

    override fun save(generatedTemplate: GeneratedTemplate): GeneratedTemplate? {
        return generatedTemplatedRepository.save(generatedTemplate)
    }
}