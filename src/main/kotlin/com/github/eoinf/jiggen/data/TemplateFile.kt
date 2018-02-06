package com.github.eoinf.jiggen.data

import org.hibernate.annotations.NamedQuery
import org.springframework.data.jpa.repository.Query
import java.io.Serializable
import javax.persistence.Basic
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

class TemplateFile() : Serializable {
    constructor(imageId: String): this() {
        this.imageId = imageId
    }

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    var id: Int? = null

    @Basic
    var imageId: String? = null

    var name: String? = null

    var extension: String? = null
}
