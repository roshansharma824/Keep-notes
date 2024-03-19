package com.example.keepnotes.utils

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController


val NavHostController.canGoBack: Boolean
    get() = this.currentBackStackEntry?.getLifecycle()?.currentState == Lifecycle.State.RESUMED

val NavController.canGoBack: Boolean
    get() = this.currentBackStackEntry?.getLifecycle()?.currentState == Lifecycle.State.RESUMED


fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}