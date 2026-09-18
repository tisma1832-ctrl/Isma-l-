package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chats")
data class ChatEntity(
    @PrimaryKey val id: String,
    val title: String,
    val subtitle: String?,
    val avatarUrl: String?,
    val isGroup: Boolean,
    val isAiAssistant: Boolean,
    val isOnline: Boolean,
    val isPinned: Boolean,
    val isFavorite: Boolean,
    val unreadCount: Int,
    val lastMessageText: String,
    val lastMessageTime: Long,
    val lastMessageSender: String?,
    val lastMessageStatus: String,
    val membersCount: Int,
    val categoryBadge: String?
)

@Entity(tableName = "messages")
data class ChatMessageEntity(
    @PrimaryKey val id: String,
    val chatId: String,
    val senderId: String,
    val senderName: String,
    val isFromMe: Boolean,
    val content: String,
    val type: String, // TEXT, VOICE, PHOTO, VIDEO, DOCUMENT, LOCATION
    val timestamp: Long,
    val status: String, // PENDING, SENT, DELIVERED, READ
    val mediaUrl: String?,
    val mediaDurationSec: Int?,
    val mediaSizeBytes: Long?,
    val mediaFileName: String?,
    val replyToId: String?,
    val replyToText: String?,
    val isStarred: Boolean
)
