package com.michal.tictactoeonline.features.signing.data

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.michal.tictactoeonline.common.AppConstants
import com.michal.tictactoeonline.common.data.model.Player
import com.michal.tictactoeonline.common.util.Resource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID

typealias Uid = String

class PlayersDBRepository(
    private var firebaseDatabase: FirebaseDatabase
) {
    private val playersRef = firebaseDatabase.reference
        .child(AppConstants.PLAYERS_PATH)

    fun createPlayer(player: Player): Flow<Resource<Uid>> = callbackFlow {
        trySend(Resource.Loading())


        val uid = generateUID()

        playersRef.get()
            .addOnSuccessListener checkIfNameAlreadyExists@{ snapshot ->
                snapshot.children.forEach {
                    if (it.child(Player::username.name).value == player.username) {
                        trySend(Resource.Error("Player with this username already exists."))
                        return@checkIfNameAlreadyExists
                    }
                }
                val playerRef = playersRef.push()
                val newPlayer = player.copy(uid = uid)

                playerRef.setValue(newPlayer)
                    .addOnSuccessListener {
                        trySend(Resource.Success(uid))
                        return@addOnSuccessListener
                    }
                    .addOnFailureListener {
                        trySend(Resource.Error(it.message ?: AppConstants.UNKNOWN_ERROR))
                        return@addOnFailureListener
                    }
            }
            .addOnFailureListener {
                trySend(Resource.Error(it.message ?: AppConstants.UNKNOWN_ERROR))
            }
        awaitClose()
    }

    suspend fun getPlayer(username: String, password: String?): Resource<Player> {
        return try {
            val snapshot = playersRef.get().await()

            for (playerSnapshot in snapshot.children) {
                println(playerSnapshot.key)
                if (playerSnapshot.child(Player::username.name).value == username
                    && playerSnapshot.child(Player::password.name).value == password
                ) {
                    val player = playerSnapshot.getValue(Player::class.java)
                    return if (player != null) {
                        Resource.Success(player)
                    } else {
                        Resource.Error(AppConstants.UNKNOWN_ERROR)
                    }
                }
            }

            Resource.Error("Player not found.")
        } catch (e: Exception) {
            println("error $e")
            Resource.Error(e.message ?: AppConstants.UNKNOWN_ERROR)
        }
    }

    suspend fun observePlayer(uid: Uid): Flow<Resource<Player>> = callbackFlow{
            val listener = (object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (playerSnapshot in snapshot.children) {
                        if (playerSnapshot.child(Player::uid.name).value == uid) {
                            val player = playerSnapshot.getValue(Player::class.java)
                            if (player != null) {
                                trySend(Resource.Success(player))
                            } else {
                                trySend(Resource.Error("Player not found"))
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    trySend(Resource.Error(error.message))
                }

            })

        playersRef.addValueEventListener(listener)
        awaitClose {
            playersRef.removeEventListener(listener)
        }
    }

    //TODO
    suspend fun getListOf100BestPlayers(): List<Player> {
        val list = playersRef.orderByChild(Player::winAmount.name).limitToFirst(100).get().await()
        list.children.forEach {
            println(it.value)
        }
        return listOf()
    }

    suspend fun updatePlayer(player: Player) {
        val snapshot = playersRef.orderByChild(Player::uid.name).equalTo(player.uid).get().await()
        if(snapshot.exists()){
            val child = snapshot.children.firstOrNull()
            val key = child?.key

            if(key != null){
                playersRef.child(key).updateChildren(
                    player.toMap()
                )
                println(player.inGame)
            }else{
                throw IllegalStateException("No key found for the matching player.")
            }
        }else{
            throw IllegalArgumentException("Player with UID ${player.uid} not found.")
        }

    }

    private suspend fun generateUID(): String {
        while (true) {
            val uuid = UUID.randomUUID().toString()
            val snapshot = playersRef.orderByChild(Player::uid.name).equalTo(uuid)
                .get()
                .await()
            if (snapshot.childrenCount == 0L) {
                return uuid
            }
        }

    }
}