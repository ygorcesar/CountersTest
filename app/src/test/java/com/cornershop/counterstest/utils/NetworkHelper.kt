package com.cornershop.counterstest.utils

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

const val baseUrl: String = "http://localhost:8080/"
const val CONTENT_TYPE_JSON = "application/json"

fun converterFactory(): Converter.Factory = Json.asConverterFactory(CONTENT_TYPE_JSON.toMediaType())

fun retrofit(
    baseUrl: String,
    converter: Converter.Factory
): Retrofit = Retrofit.Builder()
    .baseUrl(baseUrl)
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .addConverterFactory(converter)
    .build()

val retrofit = retrofit(baseUrl, converterFactory())