package com.github.eoinf.jiggen.dao

import com.github.eoinf.jiggen.DataMapper
import com.github.eoinf.jiggen.GeneratedTemplateRepository
import com.github.eoinf.jiggen.data.GeneratedTemplate
import com.github.eoinf.jiggen.data.GeneratedTemplateDTO
import com.github.eoinf.jiggen.data.TemplateFile
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

interface IGeneratedTemplateDao {
    fun get(): List<GeneratedTemplateDTO>
    fun get(id: UUID) : GeneratedTemplateDTO?
    fun save(generatedTemplateDTO: GeneratedTemplateDTO) : GeneratedTemplateDTO?
    fun getByTemplateId(templateId: UUID): List<GeneratedTemplateDTO>
}

@Service
class GeneratedTemplateDao(private val dataMapper: DataMapper) : IGeneratedTemplateDao {
    @Autowired lateinit var generatedTemplateRepository: GeneratedTemplateRepository

    override fun get(): List<GeneratedTemplateDTO> {
        return generatedTemplateRepository.findAll().toList().map {
            dataMapper.toGeneratedTemplateDTO(it, true)
        }
    }

    override fun get(id: UUID): GeneratedTemplateDTO? {
        val puzzleTemplate = generatedTemplateRepository.findById(id).orElse(null)
        if (puzzleTemplate == null)
            return null
        else
            return dataMapper.toGeneratedTemplateDTO(puzzleTemplate, false)
    }

    override fun save(generatedTemplateDTO: GeneratedTemplateDTO): GeneratedTemplateDTO? {
        val generatedTemplate = GeneratedTemplate(generatedTemplateDTO)
        return dataMapper.toGeneratedTemplateDTO(generatedTemplateRepository.save(generatedTemplate), false)
    }

    override fun getByTemplateId(templateId: UUID): List<GeneratedTemplateDTO> {
        return generatedTemplateRepository.findByTemplateFile(TemplateFile(templateId)).map{
            // Set isEmbedded to false here because right now
            // we only have one generated template per template anyway
            dataMapper.toGeneratedTemplateDTO(it, false)
        }
    }
}