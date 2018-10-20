package com.github.eoinf.jiggen

import com.google.gson.GsonBuilder
import org.springframework.stereotype.Service
import java.lang.reflect.Type

@Service
class JsonTransformer {
    private var gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            .setPrettyPrinting()
            .create()

    fun toJson(o: Any?): String {
        return if (o == null) {
            ""
        } else {
            gson.toJson(o)
        }
    }

    fun <T> fromJson(json: String, o: Class<T>): T {
        return gson.fromJson(json, o)
    }

    fun <T> fromJson(json: String, o: Type): T {
        return gson.fromJson(json, o)
    }
}