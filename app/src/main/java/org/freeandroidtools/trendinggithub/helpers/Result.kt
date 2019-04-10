package org.freeandroidtools.trendinggithub.helpers

sealed class Result<T>

class Success<T>(val data: T) : Result<T>()
class Error<T>(val error: Throwable) : Result<T>()
class Loading<T>(val loading: T) : Result<T>()