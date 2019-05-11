package com.github.eoinf.jiggen.dao

import com.github.eoinf.jiggen.DataMapper
import com.github.eoinf.jiggen.GeneratedTemplateRepository
import com.github.eoinf.jiggen.data.GeneratedTemplate
import com.github.eoinf.jiggen.data.GeneratedTemplateDTO
import com.github.eoinf.jiggen.data.TemplateFile
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import spark.Request
import java.util.UUID

interface IGeneratedTemplateDao {
    fun get(request: Request): List<GeneratedTemplateDTO>
    fun get(request: Request, id: UUID) : GeneratedTemplateDTO?
    fun save(request: Request?, generatedTemplateDTO: GeneratedTemplateDTO) : GeneratedTemplateDTO?
    fun getByTemplateId(request: Request, templateId: UUID): List<GeneratedTemplateDTO>
}

@Service
class GeneratedTemplateDao(private val dataMapper: DataMapper) : IGeneratedTemplateDao {
    @Autowired lateinit var generatedTemplateRepository: GeneratedTemplateRepository

    override fun get(request: Request): List<GeneratedTemplateDTO> {
        return generatedTemplateRepository.findAll().toList().map {
            dataMapper.toGeneratedTemplateDTO(request, it, true)
        }
    }

    override fun get(request: Request, id: UUID): GeneratedTemplateDTO? {
        val puzzleTemplate = generatedTemplateRepository.findById(id).orElse(null)
        if (puzzleTemplate == null)
            return null
        else
            return dataMapper.toGeneratedTemplateDTO(request, puzzleTemplate, false)
    }

    override fun save(request: Request?, generatedTemplateDTO: GeneratedTemplateDTO): GeneratedTemplateDTO? {
        val generatedTemplate = GeneratedTemplate(generatedTemplateDTO)
        val savedResource = generatedTemplateRepository.save(generatedTemplate)

        return dataMapper.toGeneratedTemplateDTO(request, savedResource, false)
    }

    override fun getByTemplateId(request: Request, templateId: UUID): List<GeneratedTemplateDTO> {
        return generatedTemplateRepository.findByTemplateFile(TemplateFile(templateId)).map{
            // Set isEmbedded to false here because right now
            // we only have one generated template per template anyway
            dataMapper.toGeneratedTemplateDTO(request, it, false)
        }
    }
}