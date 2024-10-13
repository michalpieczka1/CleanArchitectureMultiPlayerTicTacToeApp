package com.michal.tictactoeonline.data.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Session(
    val sessionName:String? = "",
    val sessionPassword:String? = "",
    val player1:Player = Player(),
    val player2: Player? = null,
    val playerCount:Int? = 1,
    val isWin:Boolean? = null,
    val isTie:Boolean? = null,
    val currentTurn: String? = null,
    val board: List<Int?> = List(9) {null},
){
    @Exclude
    fun toMap(): Map<String, Any?>{
        return mapOf(
            "sessionName" to sessionName,
            "sessionPassword" to sessionPassword,
            "player1" to player1,
            "player2" to player2,
            "playerCount" to playerCount,
            "isWin" to isWin,
            "isTie" to isTie,
            "currentTurn" to currentTurn,
            "board" to board
        )
    }
}
