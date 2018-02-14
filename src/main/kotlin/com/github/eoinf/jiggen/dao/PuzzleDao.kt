package com.github.eoinf.jiggen.dao

import com.github.eoinf.jiggen.PuzzleRepository
import com.github.eoinf.jiggen.data.FinishedPuzzle
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

interface IPuzzleDao {
    fun get() : List<FinishedPuzzle>
    fun get(id: UUID?) : FinishedPuzzle?
    fun save(puzzle: FinishedPuzzle) : FinishedPuzzle
}

class PuzzleRepoDao : IPuzzleDao {
    @Autowired lateinit var puzzleRepository: PuzzleRepository

    override fun get(id: UUID?) : FinishedPuzzle? {
        return puzzleRepository.findOne(id)
    }

    override fun get() : List<FinishedPuzzle> {
        return puzzleRepository.findAll().toList()
    }

    override fun save(puzzle: FinishedPuzzle) : FinishedPuzzle {
        return puzzleRepository.save(puzzle)
    }


}