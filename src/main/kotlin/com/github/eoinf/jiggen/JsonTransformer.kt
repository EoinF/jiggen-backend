package com.github.eoinf.jiggen

import com.google.gson.Gson

class JsonTransformer {
    private var gson = Gson()
    fun toJson(o: Any?) : String {
        return if (o == null) {
            ""
        } else {
            gson.toJson(o)
        }
    }
}