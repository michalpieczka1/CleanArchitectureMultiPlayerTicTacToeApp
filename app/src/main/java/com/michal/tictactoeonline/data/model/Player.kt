package com.michal.tictactoeonline.data.model

import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Player(
    val username: String = "",
    val password: String? = null,
    val uid: String = "",
    val symbol: String? = "X"
)