package com.michal.tictactoeonline

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.getValue
import com.michal.tictactoeonline.AppConstants.SESSIONS_PATH
import com.michal.tictactoeonline.data.model.Player
import com.michal.tictactoeonline.data.model.Session
import com.michal.tictactoeonline.util.Resource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.random.Random

class TicTacToeRepository(
) {
    private var firebaseDatabase = FirebaseDatabase.getInstance()
//    fun getAllSessions(): Flow<List<Session>> {
//    }
//
//
//


    fun getSessionById(id: Long): Flow<Session?> = callbackFlow {
        val sessionRef = firebaseDatabase.getReference()
            .child(SESSIONS_PATH)
            .child("$id")

        sessionRef.get().addOnSuccessListener { dataSnapShot ->
            trySend(dataSnapShot.getValue<Session>())
        }
        awaitClose()
    }

    fun createSession(player1: Player): Flow<Resource<Session>> = callbackFlow {
        trySend(Resource.Loading())
        val session = Session(
            sessionId = Random.nextLong(until = 10000000),
            player1 = player1,
            playerCount = 1,
            currentTurn = player1.uid
        )

        firebaseDatabase.reference
            .child(SESSIONS_PATH)
            .child(session.sessionId.toString())
            .setValue(session)
            .addOnSuccessListener {
                trySend(Resource.Success(session)).isSuccess
                close()
            }
            .addOnFailureListener {
                it.message?.let { error ->
                    trySend(Resource.Error(error)).isFailure
                    close()
                }
            }
        awaitClose()
    }

    fun removeSession(session: Session?): Flow<Resource<Boolean>> = callbackFlow {
        trySend(Resource.Loading())

        firebaseDatabase.reference
            .child(SESSIONS_PATH)
            .child("${session?.sessionId}")
            .removeValue()
            .addOnSuccessListener {
                trySend(Resource.Success(true)).isSuccess
                close()
            }
            .addOnFailureListener {
                it.message?.let { error ->
                    trySend(Resource.Error(error, false)).isFailure
                    close()
                }
            }
        awaitClose()
    }

    //
    fun updateSession(session: Session): Flow<Resource<Session>> = callbackFlow {
        trySend(Resource.Loading())

    }
//
//    suspend fun createPlayer(username:String): Player{
//
//    }

}