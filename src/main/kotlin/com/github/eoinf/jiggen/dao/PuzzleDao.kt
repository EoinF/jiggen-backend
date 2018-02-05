package com.github.eoinf.jiggen.dao

import com.github.eoinf.jiggen.PuzzleRepository
import com.github.eoinf.jiggen.data.FinishedPuzzle
import org.springframework.beans.factory.annotation.Autowired

interface IPuzzleDao {
    fun get() : Array<FinishedPuzzle>
    fun get(id: Int?) : FinishedPuzzle?
    fun post(puzzle: FinishedPuzzle) : FinishedPuzzle
}

class PuzzleRepoDao : IPuzzleDao {
    @Autowired lateinit var puzzleRepository: PuzzleRepository

    override fun get(id: Int?) : FinishedPuzzle? {
        return puzzleRepository.findOne(id)
    }

    override fun get() : Array<FinishedPuzzle> {
        return puzzleRepository.findAll().toList().toTypedArray()
    }

    override fun post(puzzle: FinishedPuzzle) : FinishedPuzzle {
        return puzzleRepository.save(puzzle)
    }


}