package com.michal.tictactoeonline.presentation.joinSession

data class JoinSessionUiState(
    val sessionName: String = "",
    val password: String = "",
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)