package com.github.eoinf.jiggen.dao

import com.github.eoinf.jiggen.JsonTransformer
import com.github.eoinf.jiggen.PuzzleExtractor.GraphEdge
import com.google.gson.reflect.TypeToken
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
class EdgeListConverter : AttributeConverter<List<GraphEdge>, String> {

    private val jsonTransformer = JsonTransformer()

    /**
     * Convert vertices to json string
     */
    override fun convertToDatabaseColumn(edges: List<GraphEdge>): String {
        return jsonTransformer.toJson(edges)
    }

    /**
     * Convert a json string of vertices to the java object
     */
    override fun convertToEntityAttribute(edges: String?): List<GraphEdge> {
        val collectionType = object : TypeToken<List<GraphEdge>>() {}.type
        return if (edges != null) {
            jsonTransformer.fromJson(edges, collectionType)
        } else {
            emptyList()
        }
    }
}