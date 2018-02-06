package com.github.eoinf.jiggen.data

import java.io.Serializable
import javax.persistence.Basic
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

class TemplateFile() : Serializable {
    constructor(image: String): this() {
        this.image = image
    }

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    var id: Int? = null

    @Basic
    var image: String? = null
}
