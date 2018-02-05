package com.github.eoinf.jiggen.dao

import com.github.eoinf.jiggen.PuzzleRepository
import com.github.eoinf.jiggen.data.CachedPuzzle
import org.springframework.beans.factory.annotation.Autowired

interface IPuzzleDao {
    fun get() : Array<CachedPuzzle>
    fun get(id: Int?) : CachedPuzzle?
    fun save(puzzle: CachedPuzzle) : CachedPuzzle
}

class PuzzleRepoDao : IPuzzleDao {
    @Autowired lateinit var puzzleRepository: PuzzleRepository

    override fun get(id: Int?) : CachedPuzzle? {
        return puzzleRepository.findOne(id)
    }

    override fun get() : Array<CachedPuzzle> {
        return puzzleRepository.findAll().toList().toTypedArray()
    }

    override fun save(puzzle: CachedPuzzle) : CachedPuzzle {
        return puzzleRepository.save(puzzle)
    }


}