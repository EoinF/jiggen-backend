package com.github.eoinf.jiggen.dao

import com.github.eoinf.jiggen.PuzzleTemplateRepository
import com.github.eoinf.jiggen.data.PuzzleTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

interface IPuzzleTemplateDao {
    fun get(): List<PuzzleTemplate>
    fun get(id: UUID) : PuzzleTemplate?
    fun save(puzzleTemplate: PuzzleTemplate) : PuzzleTemplate?
    fun getByTemplateId(templateId: UUID): List<PuzzleTemplate>
}

@Service
class PuzzleTemplateDao : IPuzzleTemplateDao {
    @Autowired lateinit var puzzleTemplateRepository: PuzzleTemplateRepository

    override fun get(): List<PuzzleTemplate> {
        return puzzleTemplateRepository.findAll().toList()
    }

    override fun get(id: UUID): PuzzleTemplate? {
        return puzzleTemplateRepository.findById(id).orElse(null)
    }

    override fun save(puzzleTemplate: PuzzleTemplate): PuzzleTemplate? {
        return puzzleTemplateRepository.save(puzzleTemplate)
    }

    override fun getByTemplateId(templateId: UUID): List<PuzzleTemplate> {
        return puzzleTemplateRepository.findByTemplateFile(templateId)
    }
}