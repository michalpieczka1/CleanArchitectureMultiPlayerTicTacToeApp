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
import kotlin.random.Random

class TicTacToeRepository(
) {
    private var firebaseDatabase = FirebaseDatabase.getInstance()
//    fun getAllSessions(): Flow<List<Session>> {
//    }
//
//
//


    fun getSessionById(id: String): Flow<Session?> = callbackFlow {
        val sessionRef = firebaseDatabase.getReference()
            .child(SESSIONS_PATH)
            .child(id)

        sessionRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot.getValue<Session>())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("test",error.message)
            }

        })
        awaitClose()
    }

    fun createSession(player1: Player, sessionName: String): Flow<Resource<Session>> = callbackFlow {
        trySend(Resource.Loading())
        val session = Session(
            sessionName = sessionName,
            player1 = player1,
            playerCount = 1,
            currentTurn = player1.uid
        )

        val sessionRef = firebaseDatabase.reference.child(SESSIONS_PATH)

        sessionRef.push()
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
            .child("${session?.sessionName}")
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
//    fun updateSession(session: Session?): Flow<Resource<Session>> = callbackFlow {
//        trySend(Resource.Loading())
//        val sessionMap = session?.toMap()
//
//        firebaseDatabase.reference
//            .child(SESSIONS_PATH)
//            .child("${session?.sessionName}")
//
//    }
//
//    suspend fun createPlayer(username:String): Player{
//
//    }

}