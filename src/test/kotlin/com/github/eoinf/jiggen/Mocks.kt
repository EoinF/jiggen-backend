package com.github.eoinf.jiggen

import com.github.eoinf.jiggen.dao.IBackgroundDao
import com.github.eoinf.jiggen.dao.IGeneratedTemplateDao
import com.github.eoinf.jiggen.dao.IPuzzleDao
import com.github.eoinf.jiggen.dao.ITemplateDao
import com.github.eoinf.jiggen.data.BackgroundFile
import com.github.eoinf.jiggen.data.FinishedPuzzle
import com.github.eoinf.jiggen.data.GeneratedTemplate
import com.github.eoinf.jiggen.data.TemplateFile
import java.util.*

val SHARED_TEMPLATE_ID = "6bca5f64-10a7-4cb0-a72b-6f4bc350f04d"

class TestPuzzleDao : IPuzzleDao {
    private var idInc = 1
    private val puzzles = HashMap<Int, FinishedPuzzle>()

    override fun get(): Array<FinishedPuzzle> {
        return puzzles.values.toList().toTypedArray()
    }

    override fun get(id: Int?): FinishedPuzzle? {
        return puzzles[id]
    }

    override fun save(puzzle: FinishedPuzzle) : FinishedPuzzle {
        val newId = idInc++
        puzzle.id = newId
        puzzles[newId] = puzzle
        return puzzle
    }
}

class TestTemplateDao : ITemplateDao {
    private val templates = HashMap<UUID, TemplateFile>()

    override fun get(): Array<TemplateFile> {
        return templates.values.toList().toTypedArray()
    }

    override fun get(id: UUID?): TemplateFile? {
        return templates[id]
    }

    override fun save(template: TemplateFile) : TemplateFile {
        templates[template.imageId!!] = template
        return template
    }

    init {
        this.save(TemplateFile(UUID.fromString(SHARED_TEMPLATE_ID)))
        this.save(TemplateFile(UUID.fromString("a3206515-0692-4a9b-9db7-8ad4ffc4b0aa")))
    }
}

class TestBackgroundDao : IBackgroundDao {
    private val backgrounds = HashMap<UUID, BackgroundFile>()

    override fun get(): Array<BackgroundFile> {
        return backgrounds.values.toList().toTypedArray()
    }

    override fun get(id: UUID?): BackgroundFile? {
        return backgrounds[id]
    }

    override fun save(background: BackgroundFile) : BackgroundFile {
        backgrounds[background.imageId] = background
        return background
    }

    init {
        this.save(BackgroundFile(UUID.fromString("61a85f23-f4db-4cc9-88b0-7347802d6d91")))
    }
}

class TestGeneratedTemplateDao : IGeneratedTemplateDao {
    private val gtemplates = HashMap<UUID, GeneratedTemplate>()

    override fun get(id: UUID?): GeneratedTemplate? {
        return gtemplates[id]
    }

    override fun save(generatedTemplate: GeneratedTemplate) : GeneratedTemplate{
        gtemplates[generatedTemplate.templateFile.imageId] = generatedTemplate
        return generatedTemplate
    }
}