package com.example.keepnotes.data.repository

import android.util.Log
import com.example.keepnotes.data.local.InMemoryCache
import com.example.keepnotes.data.local.NoteDatabase
import com.example.keepnotes.domain.model.Note
import com.example.keepnotes.domain.model.RealtimeModelResponse
import com.example.keepnotes.domain.model.ResultState
import com.example.keepnotes.domain.repository.LocalDataSource
import com.example.keepnotes.utils.Constants.NOTE
import com.example.keepnotes.utils.Constants.NOTES
import com.example.keepnotes.utils.Constants.TITLE
import com.example.keepnotes.utils.Constants.USERID
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class LocalDataSourceImpl(
    noteDatabase: NoteDatabase,
    private val realtimeDb: DatabaseReference
) : LocalDataSource {

    private val noteDao = noteDatabase.noteDao()
    override suspend fun insertNote(note: Note) {
        noteDao.insertNote(note = note)
    }

    override suspend fun updateNote(note: Note) {
        noteDao.updateNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }

    override fun getNote(id: Int) = noteDao.getNote(id)

    override fun getAllNote() = noteDao.getAllNote()
    override fun insert(item: RealtimeModelResponse.RealtimeItems): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)

            realtimeDb.child(InMemoryCache.userData.userId!!).child(NOTES).push().setValue(item)
                .addOnFailureListener {
                    trySend(ResultState.Failure(it))
                }
                .addOnCompleteListener {
                    if (it.isSuccessful)
                        trySend(ResultState.Success("Data inserted Successfully..."))
                }

            awaitClose { close() }
        }

    override fun getItems(): Flow<ResultState<List<RealtimeModelResponse>>> = callbackFlow {
        trySend(ResultState.Loading)

        val valueEvent = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = snapshot.child(InMemoryCache.userData.userId!!).child(NOTES).children.map {
                    RealtimeModelResponse(
                        item = it.getValue(RealtimeModelResponse.RealtimeItems::class.java),
                        key = it.key
                    )
                }
                Log.i("LocalDataSourceImpl", "getItems $items")
                trySend(ResultState.Success(items))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(ResultState.Failure(error.toException()))
            }
        }

        realtimeDb.addValueEventListener(valueEvent)

        awaitClose {
            realtimeDb.removeEventListener(valueEvent)
            close()
        }
    }

    override fun getItem(key: String): Flow<ResultState<RealtimeModelResponse>> = callbackFlow  {
        trySend(ResultState.Loading)

        realtimeDb.child(InMemoryCache.userData.userId!!).child(NOTES).child(key).get()
            .addOnSuccessListener {
                trySend(ResultState.Success(RealtimeModelResponse(
                    item = it.getValue(RealtimeModelResponse.RealtimeItems::class.java),
                    key = it.key
                )))
                Log.i("LocalDataSourceImpl", "getItem ${it.value}")
            }.addOnFailureListener{
                trySend(ResultState.Failure(it))
            }

        awaitClose {

            close()
        }
    }

    override fun delete(key: String): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        realtimeDb.child(key).removeValue()
            .addOnFailureListener {
                trySend(ResultState.Failure(it))
            }
            .addOnCompleteListener {
                if (it.isSuccessful)
                    trySend(ResultState.Success(""))
            }

        awaitClose {
            close()
        }
    }

    override fun update(res: RealtimeModelResponse): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        val map = HashMap<String, Any>()
        map[TITLE] = res.item?.title!!
        map[NOTE] = res.item.note!!
        map[USERID] = res.item.userId!!

        realtimeDb.child(InMemoryCache.userData.userId!!).child(NOTES).child(res.key!!).updateChildren(map)
            .addOnFailureListener {
                trySend(ResultState.Failure(it))
            }
            .addOnCompleteListener {
                if (it.isSuccessful)
                    trySend(ResultState.Success("Update successfully..."))
            }

        awaitClose {
            close()
        }
    }

}