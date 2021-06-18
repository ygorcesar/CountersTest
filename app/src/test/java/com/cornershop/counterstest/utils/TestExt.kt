package com.cornershop.counterstest.utils

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File

val JsonMockFile.json: String
    get() {
        val uri = this.javaClass.classLoader?.getResource("$path$fileName") ?: return ""
        val file = File(uri.path)
        return String(file.readBytes())
    }

inline fun <reified T> JsonMockFile.fromJson(): T = Json.decodeFromString(this.json)