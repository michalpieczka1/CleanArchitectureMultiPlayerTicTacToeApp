package com.michal.tictactoeonline.common.data.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.serialization.Serializable

@IgnoreExtraProperties
@Serializable
data class Player(
    val username: String = "",
    val password: String? = null,
    val uid: String = "",
    val symbol: String? = "X",
    val winAmount: Int = 0,
    val inGame: Boolean = false
){
    @Exclude
    fun toMap(): Map<String,Any?>{
        return mapOf(
            "username" to username,
            "password" to password,
            "uid" to uid,
            "symbol" to symbol,
            "winAmount" to winAmount,
            "inGame" to inGame
        )
    }
}
