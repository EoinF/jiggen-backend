package com.github.eoinf.jiggen;

// This will be AUTO IMPLEMENTED by Spring into a Bean called puzzleRepository
// CRUD refers Create, Read, Update, Delete

import org.springframework.data.repository.CrudRepository;

public interface PuzzleRepository extends CrudRepository<CachedPuzzle, Long> {

}