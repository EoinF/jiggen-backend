package com.github.eoinf.jiggen

import com.google.gson.Gson
import org.springframework.stereotype.Service

@Service
class JsonTransformer {
    private var gson = Gson()
    fun toJson(o: Any?) : String {
        return if (o == null) {
            ""
        } else {
            gson.toJson(o)
        }
    }
    fun <T> fromJson(json: String, o: Class<T>) : T {
        return gson.fromJson(json, o)
    }
}