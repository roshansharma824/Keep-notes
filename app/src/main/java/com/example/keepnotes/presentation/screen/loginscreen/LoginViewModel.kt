package com.example.keepnotes.presentation.screen.loginscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.keepnotes.data.auth.GoogleUser
import com.example.keepnotes.data.auth.UserData
import com.example.keepnotes.utils.Constants.USERS
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginViewModel(
    val userData: GoogleUser?
) : ViewModel() {

    var Bio by mutableStateOf("")

    var profilePicture by mutableStateOf("")

    private var firebase: FirebaseFirestore = FirebaseFirestore.getInstance()


    init {
        userData?.let {
            addUserToFirestore(it)
        }
//        viewModelScope.launch {
//            delay(5000)
//        }
    }


    private fun addUserToFirestore(user: GoogleUser) {
        viewModelScope.launch (Dispatchers.IO){
            val userQuery = firebase.collection(USERS).document(user.sub.toString()).get().await()

            if (!userQuery.exists()) {
                firebase.collection(USERS).document(user.sub.toString())
                    .set(user)
                    .await()
            }else{
                val currentUser = userQuery.toObject(UserData::class.java)
//                userData.familyName = currentUser?.bio.toString()
                profilePicture = currentUser?.profilePictureUrl.toString()
                Bio = currentUser?.bio.toString()
            }
        }
    }

}
