package com.github.eoinf.jiggen

import org.springframework.beans.factory.annotation.Autowired

interface PuzzleDao {
    fun get(id: Long) : CachedPuzzle?
    fun post(puzzle: CachedPuzzle)
}

class PuzzleRepoDao : PuzzleDao {
    @Autowired lateinit var puzzleRepository: PuzzleRepository

    override fun post(puzzle: CachedPuzzle) {
        puzzleRepository.save(puzzle)
    }


    override fun get(id: Long) : CachedPuzzle? {
        return puzzleRepository.findOne(id)
    }
}