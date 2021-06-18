package com.cornershop.counterstest.utils.data

open class CountersException(message: String = "") : RuntimeException(message)

class HttpError(
    statusCode: Int,
    requestUrl: String,
    responseBody: String
) : CountersException(
    "Server http response with error StatusCode: $statusCode on URL: $requestUrl and BODY: $responseBody"
)

object NetworkError :
    CountersException("Device isn't connected, the Internet connection appears to be offline.")

object UnauthorizedError : CountersException()