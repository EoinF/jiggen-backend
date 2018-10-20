package com.github.eoinf.jiggen;

// This will be AUTO IMPLEMENTED by Spring into a Bean called playablePuzzleRepository
// CRUD refers Create, Read, Update, Delete

import com.github.eoinf.jiggen.data.PlayablePuzzle;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PlayablePuzzleRepository extends CrudRepository<PlayablePuzzle, UUID> {

}