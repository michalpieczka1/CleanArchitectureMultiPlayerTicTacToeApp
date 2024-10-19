package com.michal.tictactoeonline.data.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Session(
    val sessionName:String = "",
    val sessionPassword:String = "",
    val currentTurn: Player = Player(),
    val player1:Player = Player(),
    val player2: Player? = null,
    val playerCount:Int = 1,
    val isWin:Boolean? = null,
    val winner:Player? = null,
    val isTie:Boolean? = null,
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
            "isWin" to isWin,
            "isTie" to isTie,
            "board" to board
        )
    }
}
