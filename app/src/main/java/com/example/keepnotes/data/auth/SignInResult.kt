package com.example.keepnotes.data.auth


data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)

data class UserData(
    val userId: String? = "",
    val username: String? = "",
    val profilePictureUrl: String? = "",
    val mail: String? = "",
    val chatList: List<String>? = emptyList(),
    val favorites: List<String>? = emptyList(),
    var blocked: List<String>? = emptyList(),
    var bio: String? = "",
//    var latestMessage: MessageData? = null,
    var token : String? = "",
    var status: String? = "",
    var statusExpiry: Long? = 0
)