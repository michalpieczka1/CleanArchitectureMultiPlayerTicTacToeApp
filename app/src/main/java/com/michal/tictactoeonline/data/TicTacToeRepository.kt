package com.michal.tictactoeonline.data

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.michal.tictactoeonline.AppConstants.SESSIONS_PATH
import com.michal.tictactoeonline.data.model.Player
import com.michal.tictactoeonline.data.model.Session
import com.michal.tictactoeonline.util.Resource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class TicTacToeRepository(
) {
    private var firebaseDatabase = FirebaseDatabase.getInstance()
//    fun getAllSessions(): Flow<List<Session>> {
//    }
//
//
//


    fun getSessionByKey(sessionKey: String): Flow<Session?> = callbackFlow {
        val sessionRef = firebaseDatabase.getReference()
            .child(SESSIONS_PATH)
            .child(sessionKey)

        sessionRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot.getValue<Session?>())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("test", error.message)
            }

        })
        awaitClose()
    }

//    fun getSessionByName(sessionName: String): Flow<Session?> = callbackFlow {
//        trySend(Resource.Loading())
//
//
//    }

    fun createSession(player1: Player, sessionName: String): Flow<Resource<Map<String, Session>>> =
        callbackFlow {
            trySend(Resource.Loading())

            val sessionRef = firebaseDatabase.reference.child(SESSIONS_PATH).push()
            val sessionKey = sessionRef.key

            val session = Session(
                sessionName = sessionName,
                player1 = player1,
                playerCount = 1,
                currentTurn = player1.uid
            )

            if (sessionKey == null) {
                trySend(Resource.Error("Failed to get session key")).isFailure
                close()
                return@callbackFlow
            }

            sessionRef.setValue(session)
                .addOnSuccessListener {
                    trySend(Resource.Success(mapOf(sessionKey to session))).isSuccess
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

    fun removeSession(sessionKey: String): Flow<Resource<Boolean>> = callbackFlow {
        trySend(Resource.Loading())

        firebaseDatabase.reference
            .child(SESSIONS_PATH)
            .child(sessionKey)
            .removeValue()
            .addOnSuccessListener {
                trySend(Resource.Success(true)).isSuccess
                close()
            }
            .addOnFailureListener {
                trySend(Resource.Error(it.message ?: "Unknown error occurred"))
            }
        awaitClose()
    }


    fun updateBoard(sessionKey: String, board: List<String>): Flow<Resource<Session?>> = callbackFlow {
        trySend(Resource.Loading())

        val firebaseRef = firebaseDatabase.reference.child(SESSIONS_PATH).child(sessionKey)

        try {
            firebaseRef.updateChildren(mapOf("board" to board)).await()
            val session = getSessionByKey(sessionKey).first()

            if(session == null){
                trySend(Resource.Error("Session that you got is null"))
                return@callbackFlow
            }
            trySend(Resource.Success(session)).isSuccess
        }catch(e:Exception){
            trySend(Resource.Error(e.message ?: "Unknown error occurred")).isFailure
        }
    }

//
//    suspend fun createPlayer(username:String): Player{
//
//    }

}