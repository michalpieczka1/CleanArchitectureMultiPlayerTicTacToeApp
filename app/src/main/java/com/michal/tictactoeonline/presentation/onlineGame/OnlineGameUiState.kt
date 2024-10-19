package com.michal.tictactoeonline.presentation.onlineGame

import com.michal.tictactoeonline.data.model.Session

data class OnlineGameUiState(
    val session: Session = Session(),
    val errorMessage: String? = null,
    val isLoading:Boolean = true,
)
