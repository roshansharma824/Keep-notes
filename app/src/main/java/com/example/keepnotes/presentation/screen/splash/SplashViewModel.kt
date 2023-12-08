package com.example.keepnotes.presentation.screen.splash

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(

): ViewModel() {

    private val _onBoardingIsCompleted = MutableStateFlow(true)
    val onBoardingIsCompleted: StateFlow<Boolean> = _onBoardingIsCompleted


}