package com.example.hrms.common

sealed class ApiResultState<out T> {
    object Loading : ApiResultState<Nothing>()
    data class Success<out T>(val data : T) : ApiResultState<T>()
    data class ApiError(val message: String) : ApiResultState<Nothing>()
    data class ServerError(val errorMessage: String) : ApiResultState<Nothing>()

}