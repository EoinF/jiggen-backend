package com.github.eoinf.jiggen;

// This will be AUTO IMPLEMENTED by Spring into a Bean called templateRepository
// CRUD refers Create, Read, Update, Delete

import com.github.eoinf.jiggen.data.TemplateFile;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface TemplateRepository extends CrudRepository<TemplateFile, UUID> {
}