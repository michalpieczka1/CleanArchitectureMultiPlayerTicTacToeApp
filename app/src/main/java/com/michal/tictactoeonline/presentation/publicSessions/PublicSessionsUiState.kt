package com.michal.tictactoeonline.presentation.publicSessions

import com.michal.tictactoeonline.data.model.Session
import com.michal.tictactoeonline.util.Resource
import kotlinx.coroutines.flow.Flow

sealed interface PublicSessionsUiState{
    data class Success(val sessions: List<Session>?) : PublicSessionsUiState
    data class Error(val message: String) : PublicSessionsUiState
    data object Loading : PublicSessionsUiState
}
