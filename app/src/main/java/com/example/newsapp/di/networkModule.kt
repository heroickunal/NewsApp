package com.example.newsapp.di

import android.content.Context
import com.example.newsapp.api.NewsService
import com.example.newsapp.repository.NewsRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.io.File
import java.util.concurrent.TimeUnit


private const val baseUrl = "https://newsapi.org/v2"

val networkModule = module {

    single { provideDefaultOkHttpClient(androidContext()) }
    single { provideRetrofit(get(), get()) }
    single { provideGson() }

    single { provideNewsService(get()) }

    single { NewsRepository(get()) }
}

fun provideDefaultOkHttpClient(context: Context): OkHttpClient {
    val loggingInterceptor = HttpLoggingInterceptor { message -> Timber.i(message) }
    loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

    val cacheFile = File(context.cacheDir, "okhttp.cache")
    val cache = Cache(cacheFile, (10 * 1000 * 1000).toLong()) // 10 MB

    return OkHttpClient.Builder()
        .connectTimeout(45, TimeUnit.SECONDS)
        .readTimeout(45, TimeUnit.SECONDS)
        .writeTimeout(45, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            var request = chain.request()
            val url: HttpUrl = request.url()
                .newBuilder()
                .addQueryParameter("apiKey", "f41776bc8cff476586b90ccd2f864c03")
                .build()
            request = request
                .newBuilder()
                .url(url)
                .build()
            chain.proceed(request)
        }
        .addInterceptor(loggingInterceptor)
        .cache(cache)
        .build()
}

fun provideGson(): Gson {
    return GsonBuilder().create()
}

fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(okHttpClient)
        .build()
}

fun provideNewsService(retrofit: Retrofit): NewsService =
    retrofit.create(NewsService::class.java)