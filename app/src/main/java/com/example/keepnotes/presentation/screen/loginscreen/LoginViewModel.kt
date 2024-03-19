package com.example.keepnotes.presentation.screen.loginscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.keepnotes.data.auth.UserData
import com.example.keepnotes.data.local.InMemoryCache
import com.example.keepnotes.utils.Constants.USERS
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginViewModel(
    val userData: UserData
) : ViewModel() {

    var Bio by mutableStateOf("")

    var profilePicture by mutableStateOf("")

    private var firebase: FirebaseFirestore = FirebaseFirestore.getInstance()


    init {
        addUserToFirestore(userData)
        InMemoryCache.userData = userData
        viewModelScope.launch {
            delay(5000)
        }
    }


    private fun addUserToFirestore(user: UserData) {
        viewModelScope.launch (Dispatchers.IO){
            val userQuery = firebase.collection(USERS).document(user.userId.toString()).get().await()

            if (!userQuery.exists()) {
                firebase.collection(USERS).document(user.userId.toString())
                    .set(user)
                    .await()
            }else{
                val currentUser = userQuery.toObject(UserData::class.java)
                userData.bio = currentUser?.bio.toString()
                profilePicture = currentUser?.profilePictureUrl.toString()
                Bio = currentUser?.bio.toString()
            }
        }
    }

}
