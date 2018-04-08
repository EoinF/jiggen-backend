package com.github.eoinf.jiggen;

import com.github.eoinf.jiggen.data.PuzzleTemplate;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface PuzzleTemplateRepository extends CrudRepository<PuzzleTemplate, UUID> {
    List<PuzzleTemplate> findByTemplateFile(UUID id);
}