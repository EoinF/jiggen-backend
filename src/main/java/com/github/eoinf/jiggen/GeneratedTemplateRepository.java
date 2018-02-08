package com.github.eoinf.jiggen;

import com.github.eoinf.jiggen.data.GeneratedTemplate;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface GeneratedTemplateRepository extends CrudRepository<GeneratedTemplate, UUID> {

}