package com.github.eoinf.jiggen.dao

import com.github.eoinf.jiggen.JsonTransformer
import com.github.eoinf.jiggen.webapp.screens.models.IntRectangle
import com.google.gson.reflect.TypeToken
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
class VertexDefinitionListConverter : AttributeConverter<Map<Int, IntRectangle>, String> {

    private val jsonTransformer = JsonTransformer()

    /**
     * Convert vertices to json string
     */
    override fun convertToDatabaseColumn(vertices: Map<Int, IntRectangle>): String {
        return jsonTransformer.toJson(vertices)
    }

    /**
     * Convert a json string of vertices to the java object
     */
    override fun convertToEntityAttribute(vertices: String): Map<Int, IntRectangle> {
        val collectionType = object : TypeToken< Map<Int, IntRectangle>>(){}.type
        return jsonTransformer.fromJson(vertices, collectionType)
    }
}