package com.github.eoinf.jiggen

import com.github.eoinf.jiggen.dao.IBackgroundDao
import com.github.eoinf.jiggen.dao.IPuzzleDao
import com.github.eoinf.jiggen.dao.ITemplateDao
import com.github.eoinf.jiggen.data.BackgroundFile
import com.github.eoinf.jiggen.data.CachedPuzzle
import com.github.eoinf.jiggen.data.TemplateFile
import java.io.File
import java.io.FileInputStream

class TestPuzzleDao : IPuzzleDao {
    private var idInc = 1
    private val puzzles = HashMap<Int, CachedPuzzle>()

    override fun get(): Array<CachedPuzzle> {
        return puzzles.values.toList().toTypedArray()
    }

    override fun get(id: Int?): CachedPuzzle? {
        return puzzles[id]
    }

    override fun save(puzzle: CachedPuzzle) : CachedPuzzle {
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

    override fun save(template: TemplateFile) : TemplateFile {
        val newId = idInc++
        template.id = newId
        templates[newId] = template
        return template
    }
    init {
        this.save(TemplateFile("template1.jpg"))
        this.save(TemplateFile("template2.jpg"))
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

    override fun save(background: BackgroundFile) : BackgroundFile {
        val newId = idInc++
        background.id = newId
        backgrounds[newId] = background
        return background
    }

    init {
        this.save(BackgroundFile("background1.jpg"))
    }
}

class TestJiggenFileManager: IJiggenFileManager {
    override fun saveBackgroundFile(stream: FileInputStream): File {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    override fun saveTemplateFile(stream: FileInputStream): File {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}