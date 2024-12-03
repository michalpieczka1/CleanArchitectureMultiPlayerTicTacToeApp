package com.michal.tictactoeonline.common.di

import com.michal.tictactoeonline.common.data.PlayerRepository
import com.michal.tictactoeonline.features.game.data.SessionsDBRepository
import com.michal.tictactoeonline.features.signing.data.PlayersDBRepository


interface AppContainer {
    val sessionsDBRepository: SessionsDBRepository
    val playerRepository: PlayerRepository
    val playersDBRepository: PlayersDBRepository
}