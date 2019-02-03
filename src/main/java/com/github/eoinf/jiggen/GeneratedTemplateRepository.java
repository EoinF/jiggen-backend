package com.github.eoinf.jiggen;

import com.github.eoinf.jiggen.data.GeneratedTemplate;
import com.github.eoinf.jiggen.data.TemplateFile;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface GeneratedTemplateRepository extends CrudRepository<GeneratedTemplate, UUID> {
    List<GeneratedTemplate> findByTemplateFile(TemplateFile templateFile);
}