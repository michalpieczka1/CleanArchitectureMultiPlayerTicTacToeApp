package com.example.tictactoe.feature_tic_tac_toe.domain.repository

import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    fun getSessions() : Flow<SessionRepository>

    suspend fun createSession()
}