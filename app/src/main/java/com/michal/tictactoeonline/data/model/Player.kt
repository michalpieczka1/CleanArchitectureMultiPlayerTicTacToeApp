package com.michal.tictactoeonline.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Player(
    val username: String = "",
    val password: String? = null,
    val uid: String = "",
    val symbol: String? = "X",
    val winAmount: Int = 0
)