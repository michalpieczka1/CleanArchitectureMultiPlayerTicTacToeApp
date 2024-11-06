package com.michal.tictactoeonline.data.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.serialization.Serializable

@IgnoreExtraProperties
@Serializable
data class Session(
    val sessionName:String = "",
    val sessionPassword:String = "",
    val currentTurn: Player? = null,
    val player1:Player? = null,
    val player2: Player? = null,
    val playerCount:Int = 0,
    val win:Boolean? = null,
    val playerLeft:Boolean = false,
    val winner:Player? = null,
    val tie:Boolean? = null,
    val board: List<String> = List(9) {""},
){
    @Exclude
    fun toMap(): Map<String, Any?>{
        return mapOf(
            "sessionName" to sessionName,
            "sessionPassword" to sessionPassword,
            "currentTurn" to currentTurn,
            "player1" to player1,
            "player2" to player2,
            "playerCount" to playerCount,
            "win" to win,
            "playerLeft" to playerLeft,
            "winner" to winner,
            "tie" to tie,
            "board" to board
        )
    }
}
