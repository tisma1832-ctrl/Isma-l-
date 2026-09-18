package com.example.data.model

data class User(
    val id: String,
    val displayName: String,
    val username: String, // e.g., "@ismael"
    val phoneNumber: String,
    val countryCode: String,
    val countryFlag: String,
    val avatarUrl: String? = null,
    val bio: String = "Disponible sur KOMDÉ",
    val isVerified: Boolean = false,
    val isOnline: Boolean = true,
    val lastSeenFormatted: String = "En ligne"
)
