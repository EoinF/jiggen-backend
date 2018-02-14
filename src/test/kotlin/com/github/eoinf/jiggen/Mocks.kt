package com.github.eoinf.jiggen

import com.github.eoinf.jiggen.dao.IBackgroundDao
import com.github.eoinf.jiggen.dao.IPuzzleTemplateDao
import com.github.eoinf.jiggen.dao.IPuzzleDao
import com.github.eoinf.jiggen.dao.ITemplateDao
import com.github.eoinf.jiggen.data.BackgroundFile
import com.github.eoinf.jiggen.data.FinishedPuzzle
import com.github.eoinf.jiggen.data.PuzzleTemplate
import com.github.eoinf.jiggen.data.TemplateFile
import java.util.*

const val SHARED_TEMPLATE_ID = "6bca5f64-10a7-4cb0-a72b-6f4bc350f04d"

class TestPuzzleDao : IPuzzleDao {
    private val puzzles = HashMap<UUID, FinishedPuzzle>()

    override fun get(): List<FinishedPuzzle> {
        return puzzles.values.toList()
    }

    override fun get(id: UUID?): FinishedPuzzle? {
        return puzzles[id]
    }

    override fun save(puzzle: FinishedPuzzle) : FinishedPuzzle {
        puzzle.id = UUID.randomUUID()
        puzzles[puzzle.id] = puzzle
        return puzzle
    }
}

class TestTemplateDao : ITemplateDao {
    private val templates = HashMap<UUID, TemplateFile>()

    override fun get(): List<TemplateFile> {
        return templates.values.toList()
    }

    override fun get(id: UUID?): TemplateFile? {
        return templates[id]
    }

    override fun save(template: TemplateFile) : TemplateFile {
        templates[template.id] = template
        return template
    }

    init {
        this.save(TemplateFile(UUID.fromString(SHARED_TEMPLATE_ID)))
        this.save(TemplateFile(UUID.fromString("a3206515-0692-4a9b-9db7-8ad4ffc4b0aa")))
    }
}

class TestBackgroundDao : IBackgroundDao {
    private val backgrounds = HashMap<UUID, BackgroundFile>()

    override fun get(): List<BackgroundFile> {
        return backgrounds.values.toList()
    }

    override fun get(id: UUID?): BackgroundFile? {
        return backgrounds[id]
    }

    override fun save(background: BackgroundFile) : BackgroundFile {
        backgrounds[background.id] = background
        return background
    }

    init {
        this.save(BackgroundFile(UUID.fromString("61a85f23-f4db-4cc9-88b0-7347802d6d91")))
    }
}

class TestPuzzleTemplateDao : IPuzzleTemplateDao {
    private val puzzleTemplates = HashMap<UUID, PuzzleTemplate>()

    override fun get(): List<PuzzleTemplate> {
        return puzzleTemplates.values.toList()
    }

    override fun get(id: UUID?): PuzzleTemplate? {
        return puzzleTemplates[id]
    }

    override fun getByTemplateId(templateId: UUID?) : List<PuzzleTemplate> {
        return puzzleTemplates.values.filter { it.templateFile.id == templateId}
    }

    override fun save(puzzleTemplate: PuzzleTemplate) : PuzzleTemplate {
        puzzleTemplates[puzzleTemplate.id] = puzzleTemplate
        return puzzleTemplate
    }
}