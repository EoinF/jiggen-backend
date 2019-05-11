package com.github.eoinf.jiggen.data

import java.util.UUID

interface EntityWithId {
    fun getId(): UUID
}