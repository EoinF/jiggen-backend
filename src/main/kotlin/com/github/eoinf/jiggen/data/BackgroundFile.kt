package com.github.eoinf.jiggen.data

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class BackgroundFile(
        var name: String? = null,
        var extension: String? = null
) {
    constructor(id: UUID) : this() {
        this.id = id
    }

    @Id
    lateinit var id: UUID
}
