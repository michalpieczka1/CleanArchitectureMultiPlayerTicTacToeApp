package com.michal.tictactoeonline.data

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.michal.tictactoeonline.AppConstants
import com.michal.tictactoeonline.AppConstants.SESSIONS_PATH
import com.michal.tictactoeonline.data.model.Session
import com.michal.tictactoeonline.util.Resource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class DatabaseRepository(
    private var firebaseDatabase: FirebaseDatabase
) {

    fun observeAllSessions(): Flow<Resource<List<Session>>> = callbackFlow {
        trySend(Resource.Loading())
        val sessionRef = firebaseDatabase.reference.child(SESSIONS_PATH)

        val listener = (object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val sessionList = mutableSetOf<Session?>()
                for (childSnapshot in snapshot.children) {
                    sessionList.add(childSnapshot.getValue<Session?>())
                }
                trySend(Resource.Success(sessionList.filterNotNull().toList())).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Resource.Error(error.message)).isFailure
                close()
            }

        })
        sessionRef.addValueEventListener(listener)
        awaitClose {
            sessionRef.removeEventListener(listener)
        }
    }

    fun observeSessionByKey(sessionKey: String): Flow<Resource<Session>> = callbackFlow {
        trySend(Resource.Loading())

        val sessionRef = firebaseDatabase.reference
            .child(SESSIONS_PATH)
            .child(sessionKey)

        val listener = (object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val session = snapshot.getValue<Session>()

                if (session == null) {
                    trySend(Resource.Error("Getting session returned null")).isFailure
                    close()
                    return
                }

                if(session.win == true || session.tie == true){
                    trySend(Resource.Success(session)).isSuccess
                    close()
                    return
                }else{
                    trySend(Resource.Success(session)).isSuccess
                }


            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Resource.Error(error.message)).isFailure
                close()
            }

        })
        sessionRef.addValueEventListener(listener)
        awaitClose {
            sessionRef.removeEventListener(listener)
        }
    }
    fun getSessionByKey(sessionKey: String): Flow<Resource<Session>> = callbackFlow {
        trySend(Resource.Loading())

        firebaseDatabase.reference
            .child(SESSIONS_PATH)
            .child(sessionKey)
            .get()
            .addOnSuccessListener { sessionSnapshot ->
                val session = sessionSnapshot.getValue<Session>()
                if (session == null) {
                    trySend(Resource.Error("Getting session returned null")).isFailure
                    close()
                    return@addOnSuccessListener
                }

                trySend(Resource.Success(session)).isSuccess
                close()
            }

            .addOnFailureListener { errorSnapshot ->
                trySend(Resource.Error(errorSnapshot.message ?: AppConstants.UNKNOWN_ERROR)).isFailure
                close()
            }

        awaitClose()
    }

    fun getSessionKeyByNameAndPassword(
        sessionName: String,
        password: String
    ): Flow<Resource<String>> = callbackFlow {
        trySend(Resource.Loading())

        val sessionRef = firebaseDatabase.reference
            .child(SESSIONS_PATH)

        val listener = (object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (sessionSnapshot in snapshot.children) {
                    if (sessionSnapshot.child("sessionName").getValue<String>().equals(sessionName)
                        && sessionSnapshot.child("sessionPassword").getValue<String>().equals(password)
                    ) {
                        val sessionKey = sessionSnapshot.key

                        if (sessionKey == null) {
                            trySend(Resource.Error("Getting session returned null")).isFailure
                            close()
                            return
                        }
                        trySend(Resource.Success(sessionKey)).isSuccess
                        close()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Resource.Error(error.message)).isFailure
                close()
            }

        })

        sessionRef.addListenerForSingleValueEvent(listener)
        awaitClose {
            sessionRef.removeEventListener(listener)
        }
    }

    fun createSession(
        sessionName: String,
        password: String?
    ): Flow<Resource<String>> =
        callbackFlow {
            trySend(Resource.Loading())


            val query = firebaseDatabase.reference.child(SESSIONS_PATH).orderByChild("sessionName")
                .equalTo(sessionName)

            query.get()
                .addOnSuccessListener { snapshot ->
                    val childrenCount = snapshot.childrenCount
                    if (childrenCount > 0) {
                        trySend(Resource.Error(AppConstants.ALREADY_EXISTING_SESSION)).isFailure
                        close()
                        return@addOnSuccessListener
                    }

                    val sessionRef = firebaseDatabase.reference
                        .child(SESSIONS_PATH).push()
                    val sessionKey = sessionRef.key

                    if (sessionKey == null) {
                        trySend(Resource.Error("Failed to get session key")).isFailure
                        close()
                        return@addOnSuccessListener
                    }

                    val session = Session(
                        sessionName = sessionName,
                        sessionPassword = password ?: "",
                        playerCount = 1,
                    )

                    sessionRef.setValue(session)
                        .addOnSuccessListener {
                            trySend(Resource.Success(sessionKey)).isSuccess
                            close()
                        }
                        .addOnFailureListener {
                            trySend(
                                Resource.Error(
                                    it.message ?: AppConstants.UNKNOWN_ERROR
                                )
                            ).isFailure
                            close()

                        }
                }
                .addOnFailureListener { error ->
                    trySend(Resource.Error(error.message ?: AppConstants.UNKNOWN_ERROR)).isFailure
                    close()
                }
            awaitClose()
        }

    fun removeSession(sessionKey: String) {

        firebaseDatabase.reference
            .child(SESSIONS_PATH)
            .child(sessionKey)
            .removeValue()
            .addOnSuccessListener {
                Log.i("successes", "removed session")
            }
            .addOnFailureListener {
                Log.i("errors", "error removing session")
            }

    }


    fun updateSession(sessionKey: String, session: Session) {
        val firebaseRef = firebaseDatabase.reference.child(SESSIONS_PATH).child(sessionKey)

        firebaseRef.updateChildren(session.toMap())

    }

//
//    suspend fun createPlayer(username:String): Player{
//
//    }

}