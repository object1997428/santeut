//package com.santeut.data.model
//
//sealed interface ApiResult<out T> {
//    data class Success<T>(val data: T) : ApiResult<T>
//
//    sealed interface Failure : ApiResult<Nothing> {
//        data class HttpError(val code: Int, val message: String, val boty: String) : Failure
//        // 에러 추가
//    }
//}
//
//inline fun <T> ApiResult<T>.onSuccess(
//    action: (value: T) -> Unit
//): ApiResult<T> {
//    if(isSuccess()) action(getOrThrow())
//    return this
//}
//
//inline fun <T> ApiResult<T>.onFailure(
//    action: (error: ApiResult.Failure) -> Unit
//): ApiResult<T> {
//    if(isFailure()) action(failureOrThrow())
//    return this
//}