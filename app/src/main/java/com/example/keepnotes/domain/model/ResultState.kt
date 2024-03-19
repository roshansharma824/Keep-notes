package com.example.keepnotes.domain.model

sealed class ResultState<out T> {
    data class Success<out R>(val data: R) : ResultState<R>()
    data class Failure(val msg: Throwable) : ResultState<Nothing>()
    data object Loading : ResultState<Nothing>()
}