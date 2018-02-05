package com.github.eoinf.jiggen.data

import java.io.Serializable
import javax.persistence.Basic
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

class BackgroundFile() : Serializable {
    constructor(filename: String): this() {
        this.filename = filename
    }

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    var id: Int? = null

    @Basic
    var filename: String? = null
}
