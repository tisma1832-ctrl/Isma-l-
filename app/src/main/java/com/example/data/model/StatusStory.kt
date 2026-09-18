package com.example.data.model

data class StatusStory(
    val id: String,
    val userId: String,
    val userName: String,
    val userAvatar: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val textCaption: String,
    val mediaType: MessageType = MessageType.TEXT,
    val mediaUrl: String? = null,
    val backgroundColorHex: Long = 0xFF1E293B,
    val isViewed: Boolean = false,
    val viewsCount: Int = 12
) {
    val formattedAge: String
        get() {
            val minutes = (System.currentTimeMillis() - timestamp) / (60 * 1000)
            return when {
                minutes < 1 -> "À l'instant"
                minutes < 60 -> "Il y a $minutes min"
                else -> "Il y a ${minutes / 60} h"
            }
        }
}
