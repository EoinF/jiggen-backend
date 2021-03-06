package com.github.eoinf.jiggen;

// This will be AUTO IMPLEMENTED by Spring into a Bean called backgroundRepository
// CRUD refers Create, Read, Update, Delete

import com.github.eoinf.jiggen.data.BackgroundFile;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.UUID;

public interface BackgroundRepository extends CrudRepository<BackgroundFile, UUID> {
    Iterable<BackgroundFile> findAllByReleaseDateBefore(Date limit);
}