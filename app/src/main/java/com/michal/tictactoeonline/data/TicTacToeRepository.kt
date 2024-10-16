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


    fun getAllPublicSessions(): Flow<Resource<List<Session>>> = callbackFlow {
        trySend(Resource.Loading())

        val sessionRef = firebaseDatabase.reference.child(SESSIONS_PATH)
        sessionRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val session = snapshot.getValue<List<Session>>()
                if(session != null){
                    trySend(Resource.Success(session)).isSuccess
                    close()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Resource.Error(error.message)).isFailure
                close()
            }

        })
        awaitClose()
    }

    fun getSessionByKey(sessionKey: String): Flow<Resource<Session>> = callbackFlow {
        trySend(Resource.Loading())

        val sessionRef = firebaseDatabase.reference
            .child(SESSIONS_PATH)
            .child(sessionKey)

        sessionRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val session = snapshot.getValue<Session>()

                if(session == null){
                    trySend(Resource.Error("Getting session returned null")).isFailure
                    close()
                    return
                }

                trySend(Resource.Success(session))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Resource.Error(error.message))
            }

        })
        awaitClose()
    }

    fun getSessionByNameAndPassword(sessionName: String,password: String?): Flow<Resource<Session>> = callbackFlow {
        val sessionRef = firebaseDatabase.reference
            .child(SESSIONS_PATH).orderByChild("sessionName").equalTo(sessionName).limitToFirst(1)

        sessionRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val session = snapshot.getValue<Session>()

                if(session == null){
                    trySend(Resource.Error("Getting session returned null")).isFailure
                    close()
                    return
                }
                if((password ?: "") != session.sessionPassword){
                    trySend(Resource.Error("Provided password is wrong")).isFailure
                    close()
                }

                trySend(Resource.Success(session)).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Resource.Error(error.message)).isFailure
            }

        })

        awaitClose()
    }

    fun createSession(player1: Player, sessionName: String, password:String?, isPrivate: Boolean): Flow<Resource<Map<String, Session>>> =
        callbackFlow {
            trySend(Resource.Loading())

            val sessionRef = firebaseDatabase.reference
                .child(SESSIONS_PATH).push()
            val sessionKey = sessionRef.key

            val session = Session(
                sessionName = sessionName,
                sessionPassword = password ?: "",
                isPrivate = isPrivate,
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
                close()
            }
        awaitClose()
    }


    fun updateBoard(sessionKey: String, board: List<String>): Flow<Resource<String>> = callbackFlow {
        trySend(Resource.Loading())

        val firebaseRef = firebaseDatabase.reference.child(SESSIONS_PATH).child(sessionKey)

        try {
            firebaseRef.updateChildren(mapOf("board" to board)).await()
            Resource.Success("Board updated")
            close()
        }catch(e:Exception){
            trySend(Resource.Error(e.message ?: "Unknown error occurred")).isFailure
            close()
        }
        awaitClose()
    }

//
//    suspend fun createPlayer(username:String): Player{
//
//    }

}