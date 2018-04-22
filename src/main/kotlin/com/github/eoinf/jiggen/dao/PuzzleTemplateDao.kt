package com.github.eoinf.jiggen.dao

import com.github.eoinf.jiggen.DataMapper
import com.github.eoinf.jiggen.PuzzleTemplateRepository
import com.github.eoinf.jiggen.data.PuzzleTemplate
import com.github.eoinf.jiggen.data.PuzzleTemplateDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

interface IPuzzleTemplateDao {
    fun get(): List<PuzzleTemplateDTO>
    fun get(id: UUID) : PuzzleTemplateDTO?
    fun save(puzzleTemplateDTO: PuzzleTemplateDTO) : PuzzleTemplateDTO?
    fun getByTemplateId(templateId: UUID): List<PuzzleTemplateDTO>
}

@Service
class PuzzleTemplateDao(private val dataMapper: DataMapper) : IPuzzleTemplateDao {
    @Autowired lateinit var puzzleTemplateRepository: PuzzleTemplateRepository

    override fun get(): List<PuzzleTemplateDTO> {
        return puzzleTemplateRepository.findAll().toList().map {
            dataMapper.toPuzzleTemplateDTO(it)
        }
    }

    override fun get(id: UUID): PuzzleTemplateDTO? {
        val puzzleTemplate = puzzleTemplateRepository.findById(id).orElse(null)
        if (puzzleTemplate == null)
            return null
        else
            return dataMapper.toPuzzleTemplateDTO(puzzleTemplate, depth = 2)
    }

    override fun save(puzzleTemplateDTO: PuzzleTemplateDTO): PuzzleTemplateDTO? {
        val puzzleTemplate = PuzzleTemplate(puzzleTemplateDTO)
        return dataMapper.toPuzzleTemplateDTO(puzzleTemplateRepository.save(puzzleTemplate))
    }

    override fun getByTemplateId(templateId: UUID): List<PuzzleTemplateDTO> {
        return puzzleTemplateRepository.findByTemplateFile(templateId).map{
            dataMapper.toPuzzleTemplateDTO(it)
        }
    }
}