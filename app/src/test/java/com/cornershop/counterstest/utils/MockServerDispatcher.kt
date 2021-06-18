package com.cornershop.counterstest.utils

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

class MockServerDispatcher : Dispatcher() {

    companion object {
        private const val METHOD_POST = "POST"
        private const val METHOD_DELETE = "DELETE"

        //region counters service
        private const val URL_COUNTERS = "/v1/counters"
        private const val URL_COUNTER = "/v1/counter"
        private const val URL_COUNTER_INCREMENT = "/v1/counter/inc"
        private const val URL_COUNTER_DECREMENT = "/v1/counter/dec"
        //endregion
    }

    override fun dispatch(request: RecordedRequest): MockResponse {
        return when {
            isGetCounters(request) -> getCounters()
            isIncrementCounters(request) -> incrementCounter()
            isDecrementCounters(request) -> decrementCounter()
            isAddCounter(request) -> addCounter()
            isDeleteCounter(request) -> deleteCounter()
            else -> MockResponse().setResponseCode(404)
        }
    }

    private fun successResponse() = MockResponse().setResponseCode(200)

    // region counters
    private fun getCounters() = successResponse().setBody(JsonFile.Counters.COUNTERS.json)
    private fun addCounter() = successResponse().setBody(JsonFile.Counters.ADD_COUNTER.json)
    private fun deleteCounter() = successResponse().setBody(JsonFile.Counters.DELETE_COUNTER.json)
    private fun incrementCounter() = successResponse().setBody(JsonFile.Counters.INCREMENT_COUNTER.json)
    private fun decrementCounter() = successResponse().setBody(JsonFile.Counters.DECREMENT_COUNTER.json)

    private fun isGetCounters(request: RecordedRequest) =
        (request.path ?: "").startsWith(URL_COUNTERS)

    private fun isAddCounter(request: RecordedRequest) =
        (request.path ?: "").startsWith(URL_COUNTER) && METHOD_POST == request.method

    private fun isDeleteCounter(request: RecordedRequest) =
        (request.path ?: "").startsWith(URL_COUNTER) && METHOD_DELETE == request.method

    private fun isIncrementCounters(request: RecordedRequest) =
        (request.path ?: "").startsWith(URL_COUNTER_INCREMENT)

    private fun isDecrementCounters(request: RecordedRequest) =
        (request.path ?: "").startsWith(URL_COUNTER_DECREMENT)
    //endregion
}


