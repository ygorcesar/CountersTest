package com.cornershop.counterstest.di

import android.content.Context
import com.cornershop.counterstest.BuildConfig
import com.cornershop.counterstest.utils.HttpError
import com.cornershop.counterstest.utils.NetworkHandler
import com.cornershop.counterstest.utils.UnauthorizedError
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private val CONTENT_TYPE_JSON = "application/json".toMediaType()

    @Provides
    fun provideRetrofit(
        httpClient: OkHttpClient,
        baseUrl: String,
        converter: Converter.Factory
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(converter)
        .client(httpClient)
        .build()

    @Provides
    fun provideConverter(): Converter.Factory = Json.asConverterFactory(CONTENT_TYPE_JSON)

    @Provides
    fun provideNetworkTimeout(): Long = 30L

    @Provides
    fun provideBaseUrl(): String = BuildConfig.API_URL

    @Provides
    fun provideLogger(): HttpLoggingInterceptor = HttpLoggingInterceptor(Timber::d)

    @Provides
    fun provideNetworkHandler(@ApplicationContext context: Context): NetworkHandler =
        NetworkHandler(context)

    @Provides
    fun provideClient(
        networkTimeoutSecond: Long,
        logger: HttpLoggingInterceptor,
        networkInterceptor: NetworkInterceptor
    ): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
        okHttpClientBuilder.readTimeout(networkTimeoutSecond, TimeUnit.SECONDS)
        okHttpClientBuilder.connectTimeout(networkTimeoutSecond, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            logger.level = HttpLoggingInterceptor.Level.BODY
            okHttpClientBuilder.addInterceptor(logger)
        }

        okHttpClientBuilder.addInterceptor(networkInterceptor)

        return okHttpClientBuilder.build()
    }

    @Singleton
    class NetworkInterceptor @Inject constructor() : Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val response = chain.proceed(request)
            when (response.code) {
                in 200..206 -> Unit
                401 -> throw UnauthorizedError
                else -> {
                    val requestUrl = request.url.toUri()
                    val responseBody = response.body?.string() ?: "EMPTY_BODY"
                    throw HttpError(response.code, requestUrl.toString(), responseBody)
                }
            }
            return response
        }
    }
}

