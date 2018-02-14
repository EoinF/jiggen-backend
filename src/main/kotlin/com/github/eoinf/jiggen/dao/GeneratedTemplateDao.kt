package com.github.eoinf.jiggen.dao

import com.github.eoinf.jiggen.PuzzleTemplateRepository
import com.github.eoinf.jiggen.data.PuzzleTemplate
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

interface IPuzzleTemplateDao {
    fun get(): List<PuzzleTemplate>
    fun get(id: UUID?) : PuzzleTemplate?
    fun save(puzzleTemplate: PuzzleTemplate) : PuzzleTemplate?
    fun getByTemplateId(templateId: UUID?): List<PuzzleTemplate>
}

class PuzzleTemplateDao : IPuzzleTemplateDao {
    @Autowired lateinit var puzzleTemplateRepository: PuzzleTemplateRepository

    override fun get(): List<PuzzleTemplate> {
        return puzzleTemplateRepository.findAll().toList()
    }

    override fun get(id: UUID?): PuzzleTemplate? {
        return puzzleTemplateRepository.findOne(id)
    }

    override fun save(puzzleTemplate: PuzzleTemplate): PuzzleTemplate? {
        return puzzleTemplateRepository.save(puzzleTemplate)
    }

    override fun getByTemplateId(templateId: UUID?): List<PuzzleTemplate> {
        return puzzleTemplateRepository.findByTemplateId(templateId)
    }
}