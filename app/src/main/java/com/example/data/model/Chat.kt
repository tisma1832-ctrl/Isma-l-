package com.example.data.model

data class Chat(
    val id: String,
    val title: String,
    val subtitle: String? = null,
    val avatarUrl: String? = null,
    val isGroup: Boolean = false,
    val isCommunity: Boolean = false,
    val isAiAssistant: Boolean = false,
    val isOnline: Boolean = false,
    val isPinned: Boolean = false,
    val isFavorite: Boolean = false,
    val isMuted: Boolean = false,
    val unreadCount: Int = 0,
    val lastMessageText: String = "",
    val lastMessageTime: Long = System.currentTimeMillis(),
    val lastMessageSender: String? = null,
    val lastMessageStatus: MessageStatus = MessageStatus.READ,
    val membersCount: Int = 1,
    val categoryBadge: String? = null
) {
    val formattedTime: String
        get() {
            val now = System.currentTimeMillis()
            val diff = now - lastMessageTime
            val date = java.util.Date(lastMessageTime)
            return when {
                diff < 60 * 1000 -> "À l'instant"
                diff < 24 * 3600 * 1000 -> {
                    val format = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
                    format.format(date)
                }
                diff < 48 * 3600 * 1000 -> "Hier"
                else -> {
                    val format = java.text.SimpleDateFormat("dd/MM", java.util.Locale.getDefault())
                    format.format(date)
                }
            }
        }
}
