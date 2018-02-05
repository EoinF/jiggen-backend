package com.github.eoinf.jiggen

import org.springframework.beans.factory.annotation.Value
import java.io.File
import java.io.FileInputStream
import java.util.*

interface IJiggenFileManager {
    fun saveTemplateFile(stream: FileInputStream) : File
    fun saveBackgroundFile(stream: FileInputStream) : File
}

class JiggenFileManager(@Value("jiggen.location.templates")
                        private var templatesFolder: String,
                        @Value("jiggen.location.backgrounds")
                        private var backgroundsFolder: String) : IJiggenFileManager {
    override fun saveTemplateFile(stream: FileInputStream): File {
        val fileName = "template" + UUID.randomUUID()
        val file = File(templatesFolder + fileName)
        saveFile(stream, file)
        return file
    }

    override fun saveBackgroundFile(stream: FileInputStream) : File {
        val fileName = "background" + UUID.randomUUID()
        val file = File(backgroundsFolder + fileName)
        saveFile(stream, file)
        return file
    }

    private fun saveFile(stream: FileInputStream, file: File) {
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)

        while(stream.read(buffer) != 0) {
            file.writeBytes(buffer)
        }
    }
}