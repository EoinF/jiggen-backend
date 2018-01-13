package com.github.eoinf.jiggen

/*
    Entry point for running the app locally for dev testing
 */
fun main(args: Array<String>) {
    val app = Application(TestPuzzleDao(), JsonTransformer())
    app.init()
}

class TestPuzzleDao : PuzzleDao {
    override fun get(id: Long): CachedPuzzle? {
        var puzzle = CachedPuzzle()
        puzzle.id = 5
        return puzzle
    }

    override fun post(puzzle: CachedPuzzle) {
        // Do nothing
    }
}