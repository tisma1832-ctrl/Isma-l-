package com.example.data.model

enum class NetworkMode(val label: String, val badge: String, val speedDesc: String) {
    ONLINE_4G_5G("Connexion Normale", "🟢 4G/Fibre", "Débit optimal"),
    ONLINE_WEAK_3G("Réseau Faible / 2G-3G", "🟡 Réseau Faible", "Mode résilient activé"),
    OFFLINE("Hors Ligne / Coupure", "🔴 Hors Connexion", "Messages placés en attente")
}

data class DataSaverConfig(
    val isEnabled: Boolean = true,
    val compressPhotosAggressively: Boolean = true,
    val compressAudioVoiceNotes: Boolean = true,
    val autoDownloadMediaOnWifiOnly: Boolean = true,
    val reduceAnimations: Boolean = true,
    val totalDataSavedMb: Float = 148.5f,
    val currentSessionSavedMb: Float = 12.4f,
    val networkMode: NetworkMode = NetworkMode.ONLINE_WEAK_3G
)
