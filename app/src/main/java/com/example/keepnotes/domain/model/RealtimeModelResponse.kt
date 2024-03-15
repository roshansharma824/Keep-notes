package com.example.keepnotes.domain.model

data class RealtimeModelResponse(
    val item: RealtimeItems?,
    val key: String? = ""
) {
    data class RealtimeItems(
        val userId: String? = "",
        val title: String? = "",
        val note: String? = ""
    )
}
