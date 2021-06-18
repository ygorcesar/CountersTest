package com.cornershop.counterstest.utils

data class JsonMockFile(val path: String, val fileName: String)

object JsonFile {

    object Counters {
        private const val PATH = "counters/"

        val COUNTERS = JsonMockFile(PATH, "counters.json")
        val ADD_COUNTER = JsonMockFile(PATH, "add-counter.json")
        val INCREMENT_COUNTER = JsonMockFile(PATH, "increment-counter.json")
        val DECREMENT_COUNTER = JsonMockFile(PATH, "decrement-counter.json")
        val DELETE_COUNTER = JsonMockFile(PATH, "deleted-counter.json")
    }
}
