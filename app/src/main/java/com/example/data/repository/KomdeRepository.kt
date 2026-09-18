package com.example.data.repository

import com.example.data.local.ChatEntity
import com.example.data.local.ChatMessageEntity
import com.example.data.local.KomdeDao
import com.example.data.model.AppLanguage
import com.example.data.model.Chat
import com.example.data.model.Message
import com.example.data.model.MessageReaction
import com.example.data.model.MessageStatus
import com.example.data.model.MessageType
import com.example.data.model.NetworkMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.UUID

class KomdeRepository(private val dao: KomdeDao) {

    val allChats: Flow<List<Chat>> = dao.getAllChats().map { entities ->
        entities.map { it.toModel() }
    }

    fun getMessages(chatId: String): Flow<List<Message>> =
        dao.getMessagesForChat(chatId).map { entities ->
            entities.map { it.toModel() }
        }

    suspend fun markChatAsRead(chatId: String) {
        dao.markChatAsRead(chatId)
    }

    suspend fun togglePinChat(chatId: String) {
        dao.togglePinChat(chatId)
    }

    suspend fun toggleStarMessage(messageId: String) {
        dao.toggleStarMessage(messageId)
    }

    suspend fun deleteMessage(messageId: String) {
        dao.deleteMessage(messageId)
    }

    suspend fun sendMessage(
        chatId: String,
        content: String,
        type: MessageType = MessageType.TEXT,
        mediaUrl: String? = null,
        mediaDurationSec: Int? = null,
        mediaSizeBytes: Long? = null,
        mediaFileName: String? = null,
        replyToId: String? = null,
        replyToText: String? = null,
        networkMode: NetworkMode = NetworkMode.ONLINE_4G_5G
    ): String {
        val messageId = "msg_" + UUID.randomUUID().toString().take(8)
        val initialStatus = if (networkMode == NetworkMode.OFFLINE) {
            MessageStatus.PENDING
        } else {
            MessageStatus.SENT
        }

        val entity = ChatMessageEntity(
            id = messageId,
            chatId = chatId,
            senderId = "me",
            senderName = "Moi",
            isFromMe = true,
            content = content,
            type = type.name,
            timestamp = System.currentTimeMillis(),
            status = initialStatus.name,
            mediaUrl = mediaUrl,
            mediaDurationSec = mediaDurationSec,
            mediaSizeBytes = mediaSizeBytes,
            mediaFileName = mediaFileName,
            replyToId = replyToId,
            replyToText = replyToText,
            isStarred = false
        )

        dao.insertMessage(entity)

        // Update chat's last message
        val displaySnippet = when (type) {
            MessageType.TEXT -> content
            MessageType.VOICE -> "🎤 Message vocal (${mediaDurationSec ?: 15}s)"
            MessageType.PHOTO -> "📷 Photo"
            MessageType.VIDEO -> "🎥 Vidéo"
            MessageType.DOCUMENT -> "📄 ${mediaFileName ?: "Document"}"
            MessageType.LOCATION -> "📍 Localisation"
        }

        val existing = dao.getChatById(chatId)
        if (existing != null) {
            dao.updateChat(
                existing.copy(
                    lastMessageText = displaySnippet,
                    lastMessageTime = System.currentTimeMillis(),
                    lastMessageSender = "Moi",
                    lastMessageStatus = initialStatus.name
                )
            )
        }

        // Simulate delivery and auto-response if online
        if (networkMode != NetworkMode.OFFLINE) {
            CoroutineScope(Dispatchers.IO).launch {
                delay(900)
                dao.updateMessageStatus(messageId, MessageStatus.DELIVERED.name)
                delay(1200)
                dao.updateMessageStatus(messageId, MessageStatus.READ.name)

                // If message sent to KOMDÉ AI, generate automatic smart response
                if (chatId == "chat_ai") {
                    generateAiReply(chatId, content)
                }
            }
        }

        return messageId
    }

    suspend fun syncPendingMessages(chatId: String) {
        dao.flushPendingMessages(chatId)
    }

    private suspend fun generateAiReply(chatId: String, userMessage: String) {
        delay(1500) // Realistic typing
        val lower = userMessage.lowercase()
        val replyContent = when {
            lower.contains("bonjour") || lower.contains("salut") || lower.contains("ne y") ->
                "Ne y beeoogo ! Bonjour ! Je suis KOMDÉ AI (Burkindi IA). Je suis à votre disposition pour traduire en Mòoré, Bambara ou Haoussa, rédiger un message commercial percutant ou résumer vos notes."
            lower.contains("moore") || lower.contains("mooré") || lower.contains("tradu") ->
                "✨ Traduction Mòoré :\n« Y paama laafi bala ? » (Comment allez-vous ? Tout va bien ?)\n« Barka wusgo » (Merci beaucoup)\n« Wend na kõ-d laafi » (Que Dieu nous accorde la santé)."
            lower.contains("bambara") || lower.contains("mali") ->
                "✨ Traduction Bamanankan :\n« I ni ce ! » (Bonjour / Bravo)\n« Somɔgɔw bɛ di ? » (Comment va la famille ?)\n« Ala ka hɛɛra d'an ma » (Que Dieu nous donne la paix)."
            lower.contains("hausa") || lower.contains("haoussa") ->
                "✨ Traduction Hausa :\n« Sannu da zuwa ! » (Bienvenue !)\n« Yaya aiki ? » (Comment se passe le travail ?)\n« Na gode sosai » (Merci infiniment)."
            lower.contains("data") || lower.contains("economie") ->
                "💡 Conseil Économie KOMDÉ :\nVotre mode Data Saver est actif ! Vos notes vocales sont compressées à 16 kbps en Opus (75% d'économie) et les images sont optimisées localement avant envoi pour préserver votre forfait Internet au Burkina Faso."
            else ->
                "Bien reçu ! KOMDÉ facilite vos échanges avec des outils optimisés pour nos réalités. Avez-vous besoin d'une traduction, d'un résumé de conversation ou d'un modèle de message professionnel ?"
        }

        val aiMessageId = "msg_ai_" + UUID.randomUUID().toString().take(6)
        dao.insertMessage(
            ChatMessageEntity(
                id = aiMessageId,
                chatId = chatId,
                senderId = "ai_komde",
                senderName = "KOMDÉ AI",
                isFromMe = false,
                content = replyContent,
                type = MessageType.TEXT.name,
                timestamp = System.currentTimeMillis(),
                status = MessageStatus.READ.name,
                mediaUrl = null,
                mediaDurationSec = null,
                mediaSizeBytes = null,
                mediaFileName = null,
                replyToId = null,
                replyToText = null,
                isStarred = false
            )
        )

        val chat = dao.getChatById(chatId)
        if (chat != null) {
            dao.updateChat(
                chat.copy(
                    lastMessageText = replyContent.take(60) + "...",
                    lastMessageTime = System.currentTimeMillis(),
                    lastMessageSender = "KOMDÉ AI",
                    lastMessageStatus = MessageStatus.READ.name
                )
            )
        }
    }

    suspend fun seedInitialDataIfEmpty() {
        val existingChats = dao.getChatById("chat_creator")
        if (existingChats != null) return

        val initialChats = listOf(
            ChatEntity(
                id = "chat_creator",
                title = "ISMAËL REGTOUMDA",
                subtitle = "Concepteur du projet KOMDÉ • Ouagadougou",
                avatarUrl = null,
                isGroup = false,
                isAiAssistant = false,
                isOnline = true,
                isPinned = true,
                isFavorite = true,
                unreadCount = 1,
                lastMessageText = "Bienvenue sur KOMDÉ ! Notre plateforme est conçue pour l'Afrique.",
                lastMessageTime = System.currentTimeMillis() - 5 * 60 * 1000,
                lastMessageSender = "ISMAËL REGTOUMDA",
                lastMessageStatus = MessageStatus.DELIVERED.name,
                membersCount = 1,
                categoryBadge = "Créateur"
            ),
            ChatEntity(
                id = "chat_ai",
                title = "KOMDÉ AI (Burkindi IA)",
                subtitle = "Assistant intelligent • Traduction & Rédaction",
                avatarUrl = null,
                isGroup = false,
                isAiAssistant = true,
                isOnline = true,
                isPinned = true,
                isFavorite = false,
                unreadCount = 0,
                lastMessageText = "Traductions en Mooré, Bambara, Haoussa et conseils data.",
                lastMessageTime = System.currentTimeMillis() - 15 * 60 * 1000,
                lastMessageSender = "KOMDÉ AI",
                lastMessageStatus = MessageStatus.READ.name,
                membersCount = 1,
                categoryBadge = "IA Vocale"
            ),
            ChatEntity(
                id = "chat_burkina_tech",
                title = "Burkina Tech & Innovation 🇧🇫",
                subtitle = "328 membres • Souveraineté numérique & Dev",
                avatarUrl = null,
                isGroup = true,
                isAiAssistant = false,
                isOnline = true,
                isPinned = false,
                isFavorite = true,
                unreadCount = 3,
                lastMessageText = "Ousmane : Le mode hors-connexion de KOMDÉ est un atout majeur !",
                lastMessageTime = System.currentTimeMillis() - 42 * 60 * 1000,
                lastMessageSender = "Ousmane",
                lastMessageStatus = MessageStatus.READ.name,
                membersCount = 328,
                categoryBadge = "Communauté"
            ),
            ChatEntity(
                id = "chat_aminata",
                title = "Aminata Kaboré",
                subtitle = "Bobo-Dioulasso • En ligne",
                avatarUrl = null,
                isGroup = false,
                isAiAssistant = false,
                isOnline = true,
                isPinned = false,
                isFavorite = false,
                unreadCount = 0,
                lastMessageText = "🎤 Message vocal (18s)",
                lastMessageTime = System.currentTimeMillis() - 2 * 3600 * 1000,
                lastMessageSender = "Aminata",
                lastMessageStatus = MessageStatus.READ.name,
                membersCount = 1,
                categoryBadge = null
            ),
            ChatEntity(
                id = "chat_artisans",
                title = "Faso Dan Fani & Artisans Ouaga",
                subtitle = "Commerce & Création textile locale",
                avatarUrl = null,
                isGroup = true,
                isAiAssistant = false,
                isOnline = false,
                isPinned = false,
                isFavorite = false,
                unreadCount = 0,
                lastMessageText = "📄 Catalogue_Dan_Fani_2026.pdf",
                lastMessageTime = System.currentTimeMillis() - 14 * 3600 * 1000,
                lastMessageSender = "Mariam",
                lastMessageStatus = MessageStatus.READ.name,
                membersCount = 94,
                categoryBadge = "Business"
            )
        )

        dao.insertChats(initialChats)

        // Seed messages for creator chat
        val creatorMessages = listOf(
            ChatMessageEntity(
                id = "msg_c1",
                chatId = "chat_creator",
                senderId = "creator",
                senderName = "ISMAËL REGTOUMDA",
                isFromMe = false,
                content = "Ne y beeoogo ! Bienvenue sur KOMDÉ. En tant que jeune concepteur autodidacte basé à Ouagadougou, j'ai voulu créer une messagerie adaptée à nos réalités : résilience réseau, économie de forfait et vraie identité.",
                type = MessageType.TEXT.name,
                timestamp = System.currentTimeMillis() - 30 * 60 * 1000,
                status = MessageStatus.READ.name,
                mediaUrl = null,
                mediaDurationSec = null,
                mediaSizeBytes = null,
                mediaFileName = null,
                replyToId = null,
                replyToText = null,
                isStarred = true
            ),
            ChatMessageEntity(
                id = "msg_c2",
                chatId = "chat_creator",
                senderId = "creator",
                senderName = "ISMAËL REGTOUMDA",
                isFromMe = false,
                content = "Vous pouvez tester le commutateur réseau en haut : même sans connexion, vos messages se mettent en file d'attente et partent automatiquement dès le retour du réseau !",
                type = MessageType.TEXT.name,
                timestamp = System.currentTimeMillis() - 10 * 60 * 1000,
                status = MessageStatus.READ.name,
                mediaUrl = null,
                mediaDurationSec = null,
                mediaSizeBytes = null,
                mediaFileName = null,
                replyToId = null,
                replyToText = null,
                isStarred = false
            ),
            ChatMessageEntity(
                id = "msg_c3",
                chatId = "chat_creator",
                senderId = "creator",
                senderName = "ISMAËL REGTOUMDA",
                isFromMe = false,
                content = "Je reste joignable au 57 10 87 57 ou iregtoumda78@gmail.com pour tout échange et partenariat sur l'écosystème numérique africain.",
                type = MessageType.TEXT.name,
                timestamp = System.currentTimeMillis() - 5 * 60 * 1000,
                status = MessageStatus.DELIVERED.name,
                mediaUrl = null,
                mediaDurationSec = null,
                mediaSizeBytes = null,
                mediaFileName = null,
                replyToId = null,
                replyToText = null,
                isStarred = false
            )
        )

        // Seed messages for Aminata (voice note demo)
        val aminataMessages = listOf(
            ChatMessageEntity(
                id = "msg_a1",
                chatId = "chat_aminata",
                senderId = "aminata",
                senderName = "Aminata Kaboré",
                isFromMe = false,
                content = "Salam ! J'espère que tout va bien à Ouaga. Écoute cette note vocale rapide :",
                type = MessageType.TEXT.name,
                timestamp = System.currentTimeMillis() - 2 * 3600 * 1000,
                status = MessageStatus.READ.name,
                mediaUrl = null,
                mediaDurationSec = null,
                mediaSizeBytes = null,
                mediaFileName = null,
                replyToId = null,
                replyToText = null,
                isStarred = false
            ),
            ChatMessageEntity(
                id = "msg_a2",
                chatId = "chat_aminata",
                senderId = "aminata",
                senderName = "Aminata Kaboré",
                isFromMe = false,
                content = "Note vocale de Bobo-Dioulasso",
                type = MessageType.VOICE.name,
                timestamp = System.currentTimeMillis() - (2 * 3600 * 1000 - 60000),
                status = MessageStatus.READ.name,
                mediaUrl = "voice_sample.opus",
                mediaDurationSec = 18,
                mediaSizeBytes = 28000, // compressed Opus ~28KB
                mediaFileName = "vocal_aminata.opus",
                replyToId = null,
                replyToText = null,
                isStarred = true
            )
        )

        // Seed messages for Burkina Tech group
        val groupMessages = listOf(
            ChatMessageEntity(
                id = "msg_g1",
                chatId = "chat_burkina_tech",
                senderId = "user_fatou",
                senderName = "Fatoumata Traoré",
                isFromMe = false,
                content = "Félicitations pour l'initiative KOMDÉ. Enfin une plateforme qui prend au sérieux les défis de bande passante chez nous !",
                type = MessageType.TEXT.name,
                timestamp = System.currentTimeMillis() - 50 * 60 * 1000,
                status = MessageStatus.READ.name,
                mediaUrl = null,
                mediaDurationSec = null,
                mediaSizeBytes = null,
                mediaFileName = null,
                replyToId = null,
                replyToText = null,
                isStarred = false
            ),
            ChatMessageEntity(
                id = "msg_g2",
                chatId = "chat_burkina_tech",
                senderId = "user_ousmane",
                senderName = "Ousmane Diallo",
                isFromMe = false,
                content = "Le mode hors-connexion de KOMDÉ est un atout majeur pour les déplacements en province.",
                type = MessageType.TEXT.name,
                timestamp = System.currentTimeMillis() - 42 * 60 * 1000,
                status = MessageStatus.READ.name,
                mediaUrl = null,
                mediaDurationSec = null,
                mediaSizeBytes = null,
                mediaFileName = null,
                replyToId = null,
                replyToText = null,
                isStarred = false
            )
        )

        dao.insertMessages(creatorMessages + aminataMessages + groupMessages)
    }

    private fun ChatEntity.toModel(): Chat {
        return Chat(
            id = id,
            title = title,
            subtitle = subtitle,
            avatarUrl = avatarUrl,
            isGroup = isGroup,
            isAiAssistant = isAiAssistant,
            isOnline = isOnline,
            isPinned = isPinned,
            isFavorite = isFavorite,
            unreadCount = unreadCount,
            lastMessageText = lastMessageText,
            lastMessageTime = lastMessageTime,
            lastMessageSender = lastMessageSender,
            lastMessageStatus = runCatching { MessageStatus.valueOf(lastMessageStatus) }.getOrDefault(MessageStatus.READ),
            membersCount = membersCount,
            categoryBadge = categoryBadge
        )
    }

    private fun ChatMessageEntity.toModel(): Message {
        return Message(
            id = id,
            chatId = chatId,
            senderId = senderId,
            senderName = senderName,
            isFromMe = isFromMe,
            content = content,
            type = runCatching { MessageType.valueOf(type) }.getOrDefault(MessageType.TEXT),
            timestamp = timestamp,
            status = runCatching { MessageStatus.valueOf(status) }.getOrDefault(MessageStatus.SENT),
            mediaUrl = mediaUrl,
            mediaDurationSec = mediaDurationSec,
            mediaSizeBytes = mediaSizeBytes,
            mediaFileName = mediaFileName,
            voiceWaveform = if (type == MessageType.VOICE.name) listOf(0.3f, 0.6f, 0.9f, 0.4f, 0.7f, 1.0f, 0.8f, 0.5f, 0.3f, 0.8f, 0.9f, 0.6f, 0.4f, 0.7f, 0.5f, 0.3f) else emptyList(),
            replyToId = replyToId,
            replyToText = replyToText,
            reactions = if (isStarred) listOf(MessageReaction("❤️", 1, true)) else emptyList(),
            isStarred = isStarred
        )
    }
}
