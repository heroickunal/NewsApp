package com.example.newsapp.repository

import com.example.newsapp.util.ResponseHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber

abstract class BaseRepository {

    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): ResponseHandler<T> {
        return withContext(Dispatchers.IO) {
            try {
                ResponseHandler.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is HttpException -> {
                        ResponseHandler.Failure(
                            false,
                            throwable.code().toString(),
                            throwable.response()?.errorBody()
                        )
                    }
                    else -> {
                        Timber.d("throwable $throwable")
                        ResponseHandler.Failure(
                            true, null, null
                        )
                    }
                }
            }
        }
    }
}