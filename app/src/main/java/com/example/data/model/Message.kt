package com.example.data.model

enum class MessageType {
    TEXT,
    VOICE,
    PHOTO,
    VIDEO,
    DOCUMENT,
    LOCATION
}

enum class MessageStatus {
    PENDING,   // En attente (Offline / In queue)
    SENT,      // Envoyé (Server received)
    DELIVERED, // Reçu (Contact received)
    READ       // Lu (Contact opened)
}

data class MessageReaction(
    val emoji: String,
    val count: Int,
    val hasUserReacted: Boolean = false
)

data class Message(
    val id: String,
    val chatId: String,
    val senderId: String,
    val senderName: String,
    val isFromMe: Boolean,
    val content: String,
    val type: MessageType = MessageType.TEXT,
    val timestamp: Long = System.currentTimeMillis(),
    val status: MessageStatus = MessageStatus.SENT,
    // Media attributes
    val mediaUrl: String? = null,
    val mediaThumbnail: String? = null,
    val mediaDurationSec: Int? = null,
    val mediaSizeBytes: Long? = null,
    val mediaFileName: String? = null,
    val voiceWaveform: List<Float> = emptyList(),
    // Reply context
    val replyToId: String? = null,
    val replyToSender: String? = null,
    val replyToText: String? = null,
    // Reactions
    val reactions: List<MessageReaction> = emptyList(),
    val isStarred: Boolean = false,
    val isForwarded: Boolean = false
) {
    val formattedTime: String
        get() {
            val date = java.util.Date(timestamp)
            val format = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
            return format.format(date)
        }

    val formattedSize: String
        get() {
            val bytes = mediaSizeBytes ?: return ""
            return when {
                bytes < 1024 -> "$bytes B"
                bytes < 1024 * 1024 -> "${bytes / 1024} KB"
                else -> String.format(java.util.Locale.US, "%.1f MB", bytes.toFloat() / (1024 * 1024))
            }
        }
}
