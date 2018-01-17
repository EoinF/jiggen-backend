package com.github.eoinf.jiggen

import com.github.eoinf.jiggen.dao.IBackgroundDao
import com.github.eoinf.jiggen.dao.IPuzzleDao
import com.github.eoinf.jiggen.dao.ITemplateDao
import com.github.eoinf.jiggen.data.BackgroundFile
import com.github.eoinf.jiggen.data.CachedPuzzle
import com.github.eoinf.jiggen.data.TemplateFile

class TestPuzzleDao : IPuzzleDao {
    private var idInc = 1
    private val puzzles = HashMap<Int, CachedPuzzle>()

    override fun get(): Array<CachedPuzzle> {
        return puzzles.values.toList().toTypedArray()
    }

    override fun get(id: Int?): CachedPuzzle? {
        return puzzles[id]
    }

    override fun post(puzzle: CachedPuzzle) : CachedPuzzle {
        val newId = idInc++
        puzzle.id = newId
        puzzles[newId] = puzzle
        return puzzle
    }
}

class TestTemplateDao : ITemplateDao {
    private var idInc = 1
    private val templates = HashMap<Int, TemplateFile>()

    override fun get(): Array<TemplateFile> {
        return templates.values.toList().toTypedArray()
    }

    override fun get(id: Int?): TemplateFile? {
        return templates[id]
    }

    override fun post(template: TemplateFile) : TemplateFile {
        val newId = idInc++
        template.id = newId
        templates[newId] = template
        return template
    }
    init {
        this.post(TemplateFile("template1.jpg"))
        this.post(TemplateFile("template2.jpg"))
    }
}

class TestBackgroundDao : IBackgroundDao {
    private var idInc = 1
    private val backgrounds = HashMap<Int, BackgroundFile>()

    override fun get(): Array<BackgroundFile> {
        return backgrounds.values.toList().toTypedArray()
    }

    override fun get(id: Int?): BackgroundFile? {
        return backgrounds[id]
    }

    override fun post(background: BackgroundFile) : BackgroundFile {
        val newId = idInc++
        background.id = newId
        backgrounds[newId] = background
        return background
    }

    init {
        this.post(BackgroundFile("background1.jpg"))
    }
}