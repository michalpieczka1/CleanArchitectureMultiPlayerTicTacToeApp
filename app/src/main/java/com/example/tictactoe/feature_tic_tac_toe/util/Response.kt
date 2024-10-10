package com.example.tictactoe.feature_tic_tac_toe.util

sealed class Response<out T> {
    data object Loading: Response<Nothing>()

    data class Success<out T>(
        val data: T?
    ): Response<T>()

    data class Failure(
        val e: Exception?
    ): Response<Nothing>()
}