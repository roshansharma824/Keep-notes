package com.example.keepnotes.presentation.screen.loginscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.keepnotes.data.auth.SignInResult
import com.example.keepnotes.data.auth.SignInState
import com.example.keepnotes.data.local.InMemoryCache
import com.example.keepnotes.data.local.KeepNoteDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val keepNoteDataStore: KeepNoteDataStore
) : ViewModel() {

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    private val _userId = MutableStateFlow("")
    val userId = _userId.asStateFlow()

    private val _userProfileUrl = MutableStateFlow("")
    val userProfileUrl = _userProfileUrl.asStateFlow()


    init {
        getData()
    }

    private fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            keepNoteDataStore.getUserId.zip(keepNoteDataStore.getUserProfileUrl){ userId, userProfileUrl->
                if (userId.isNotEmpty()) {
                    InMemoryCache.userData.userId = userId
                    _userId.update {
                        userId
                    }
                    _state.update {
                        it.copy(
                            isSignInSuccessful = true,
                            signInError = null
                        )
                    }
                }
                if (userProfileUrl.isNotEmpty()) {
                    InMemoryCache.userData.profilrUrl = userProfileUrl
                    _userProfileUrl.update {
                        userProfileUrl
                    }
                }
            }.collect()
        }


    }


    fun saveUserId(userId: String) {
        viewModelScope.launch {
            keepNoteDataStore.saveUserId(userId)
        }
    }

    fun saveUserProfileUrl(userProfileUrl: String) {
        viewModelScope.launch {
            keepNoteDataStore.saveUserProfileUrl(userProfileUrl)
        }
    }

    fun onSignInResult(result: SignInResult) {
        _state.update {
            it.copy(
                isSignInSuccessful = result.data != null,
                signInError = result.errorMessage
            )
        }
    }

    fun resetState() {
        _state.update { SignInState() }
    }
}