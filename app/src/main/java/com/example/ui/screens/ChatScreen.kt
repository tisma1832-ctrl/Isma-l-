package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Chat
import com.example.data.model.DataSaverConfig
import com.example.data.model.Message
import com.example.data.model.MessageStatus
import com.example.data.model.MessageType
import com.example.data.model.NetworkMode
import com.example.ui.components.KomdeAvatar
import com.example.ui.components.MediaMessageCard
import com.example.ui.components.NetworkStatusBanner
import com.example.ui.components.VoiceMessageCard
import com.example.ui.theme.KomdeEmerald
import com.example.ui.theme.KomdeGold
import com.example.ui.theme.KomdeTerracotta

@Composable
fun ChatScreen(
    chat: Chat,
    messages: List<Message>,
    dataSaver: DataSaverConfig,
    isRecordingVoice: Boolean,
    recordingDurationSec: Int,
    playingVoiceMessageId: String?,
    playbackProgress: Float,
    playbackSpeed: Float,
    replyingTo: Message?,
    onBack: () -> Unit,
    onSendMessage: (String) -> Unit,
    onStartVoiceRecord: () -> Unit,
    onCancelVoiceRecord: () -> Unit,
    onStopAndSendVoiceRecord: () -> Unit,
    onTogglePlayVoice: (String, Int) -> Unit,
    onCyclePlaybackSpeed: () -> Unit,
    onSendSamplePhoto: () -> Unit,
    onSendSampleDocument: () -> Unit,
    onSendSampleLocation: () -> Unit,
    onReplyTo: (Message?) -> Unit,
    onStarMessage: (String) -> Unit,
    onDeleteMessage: (String) -> Unit,
    onNetworkModeChange: (NetworkMode) -> Unit,
    modifier: Modifier = Modifier
) {
    var textInput by remember { mutableStateOf("") }
    var showAttachmentSheet by remember { mutableStateOf(false) }
    var showOptionsMenu by remember { mutableStateOf(false) }
    var showCallDialog by remember { mutableStateOf<String?>(null) }
    var selectedMessageForAction by remember { mutableStateOf<Message?>(null) }

    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        // Network resilience banner
        NetworkStatusBanner(
            dataSaver = dataSaver,
            onNetworkModeChange = onNetworkModeChange
        )

        // Top App Bar
        Surface(
            tonalElevation = 3.dp,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("chat_back_button")) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                }

                KomdeAvatar(
                    name = chat.title,
                    size = 42.dp,
                    isGroup = chat.isGroup,
                    isAiAssistant = chat.isAiAssistant,
                    isOnline = chat.isOnline
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = chat.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            maxLines = 1,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (chat.categoryBadge != null) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = KomdeTerracotta.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = chat.categoryBadge,
                                    fontSize = 9.sp,
                                    color = KomdeTerracotta,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                    }

                    Text(
                        text = if (dataSaver.networkMode == NetworkMode.OFFLINE) "Hors-ligne • Queue active"
                        else if (chat.isOnline) "En ligne • Chiffré"
                        else chat.subtitle ?: "Disponible",
                        fontSize = 11.sp,
                        color = if (chat.isOnline && dataSaver.networkMode != NetworkMode.OFFLINE) KomdeEmerald else Color.Gray,
                        maxLines = 1
                    )
                }

                // Call buttons (Calls roadmap preview)
                IconButton(onClick = { showCallDialog = "audio" }) {
                    Icon(Icons.Default.Call, contentDescription = "Appel Audio", tint = KomdeTerracotta)
                }
                IconButton(onClick = { showCallDialog = "video" }) {
                    Icon(Icons.Default.Videocam, contentDescription = "Appel Vidéo", tint = KomdeTerracotta)
                }

                Box {
                    IconButton(onClick = { showOptionsMenu = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu options")
                    }
                    DropdownMenu(
                        expanded = showOptionsMenu,
                        onDismissRequest = { showOptionsMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Infos du contact") },
                            onClick = { showOptionsMenu = false }
                        )
                        DropdownMenuItem(
                            text = { Text("Médias & documents") },
                            onClick = { showOptionsMenu = false }
                        )
                        DropdownMenuItem(
                            text = { Text("Mode Économie pour ce chat") },
                            onClick = { showOptionsMenu = false }
                        )
                    }
                }
            }
        }

        // Offline Notification Bar
        if (dataSaver.networkMode == NetworkMode.OFFLINE) {
            Surface(
                color = Color(0xFF7F1D1D),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Schedule, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Mode hors-connexion. Vos messages sont enregistrés en local et partiront dès le retour du réseau.",
                        color = Color.White,
                        fontSize = 11.sp,
                        lineHeight = 14.sp
                    )
                }
            }
        }

        // Messages LazyColumn
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Text(
                                text = "🔒 Les messages sur KOMDÉ sont sécurisés et optimisés pour votre réseau",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(messages, key = { it.id }) { message ->
                    MessageBubble(
                        message = message,
                        isPlayingVoice = playingVoiceMessageId == message.id,
                        voiceProgress = if (playingVoiceMessageId == message.id) playbackProgress else 0f,
                        playbackSpeed = playbackSpeed,
                        onTogglePlayVoice = { onTogglePlayVoice(message.id, message.mediaDurationSec ?: 15) },
                        onCyclePlaybackSpeed = onCyclePlaybackSpeed,
                        onLongClick = { selectedMessageForAction = message },
                        onSwipeReply = { onReplyTo(message) }
                    )
                }

                item { Spacer(modifier = Modifier.height(12.dp)) }
            }
        }

        // Fast African Localized Smart Replies
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val quickReplies = listOf(
                "Ne y beeoogo ! 🇧🇫",
                "Barka wusgo (Merci)",
                "I ni ce ! 🇲🇱",
                "Sannu da aiki ! 🇳🇪",
                "D'accord, bien reçu !",
                "Je t'envoie une note vocale 🎤"
            )
            items(quickReplies) { reply ->
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.clickable { onSendMessage(reply) }
                ) {
                    Text(
                        text = reply,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }
        }

        // Reply preview bar
        AnimatedVisibility(visible = replyingTo != null) {
            replyingTo?.let { reply ->
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .width(3.dp)
                                .height(28.dp)
                                .background(KomdeTerracotta)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Réponse à ${reply.senderName}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = KomdeTerracotta)
                            Text(text = reply.content, fontSize = 11.sp, maxLines = 1, color = Color.Gray)
                        }
                        IconButton(onClick = { onReplyTo(null) }) {
                            Icon(Icons.Default.Close, contentDescription = "Fermer réponse", modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }

        // Attachment Sheet
        AnimatedVisibility(visible = showAttachmentSheet) {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp,
                shape = RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    AttachmentOption(
                        icon = Icons.Default.Image,
                        label = "Photo optimisée",
                        color = KomdeTerracotta,
                        onClick = {
                            showAttachmentSheet = false
                            onSendSamplePhoto()
                        }
                    )
                    AttachmentOption(
                        icon = Icons.Default.Description,
                        label = "Document PDF",
                        color = Color(0xFFDC2626),
                        onClick = {
                            showAttachmentSheet = false
                            onSendSampleDocument()
                        }
                    )
                    AttachmentOption(
                        icon = Icons.Default.LocationOn,
                        label = "Localisation",
                        color = KomdeEmerald,
                        onClick = {
                            showAttachmentSheet = false
                            onSendSampleLocation()
                        }
                    )
                }
            }
        }

        // Bottom Input Row / Voice Recorder
        Surface(
            tonalElevation = 4.dp,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isRecordingVoice) {
                // Live voice recording bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.FiberManualRecord,
                            contentDescription = "Enregistrement",
                            tint = Color.Red,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = String.format("%02d:%02d", recordingDurationSec / 60, recordingDurationSec % 60),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Red
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Opus 16kbps actif",
                            fontSize = 11.sp,
                            color = KomdeEmerald
                        )
                    }

                    Row {
                        IconButton(onClick = onCancelVoiceRecord) {
                            Icon(Icons.Default.Cancel, contentDescription = "Annuler", tint = Color.Gray)
                        }
                        FloatingActionButton(
                            onClick = onStopAndSendVoiceRecord,
                            containerColor = KomdeTerracotta,
                            modifier = Modifier.size(44.dp)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Envoyer vocal", tint = Color.White)
                        }
                    }
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { showAttachmentSheet = !showAttachmentSheet }) {
                        Icon(Icons.Default.AttachFile, contentDescription = "Pièce jointe", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    OutlinedTextField(
                        value = textInput,
                        onValueChange = { textInput = it },
                        placeholder = { Text("Écrire un message...", fontSize = 14.sp) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("chat_message_input"),
                        shape = RoundedCornerShape(22.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = KomdeTerracotta
                        ),
                        maxLines = 4
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    if (textInput.isNotBlank()) {
                        FloatingActionButton(
                            onClick = {
                                onSendMessage(textInput)
                                textInput = ""
                            },
                            containerColor = KomdeTerracotta,
                            modifier = Modifier
                                .size(44.dp)
                                .testTag("chat_send_button")
                        ) {
                            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Envoyer", tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                    } else {
                        FloatingActionButton(
                            onClick = onStartVoiceRecord,
                            containerColor = KomdeTerracotta,
                            modifier = Modifier
                                .size(44.dp)
                                .testTag("chat_voice_record_button")
                        ) {
                            Icon(Icons.Default.Mic, contentDescription = "Enregistrer note vocale", tint = Color.White, modifier = Modifier.size(22.dp))
                        }
                    }
                }
            }
        }
    }

    // Contextual Action Dialog for selected message
    selectedMessageForAction?.let { msg ->
        AlertDialog(
            onDismissRequest = { selectedMessageForAction = null },
            title = { Text("Message de ${msg.senderName}") },
            text = {
                Column {
                    Text(text = "« ${msg.content.take(60)} »", fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "Réactions rapides :", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        listOf("❤️", "👍", "🔥", "👏", "😂").forEach { emoji ->
                            Text(
                                text = emoji,
                                fontSize = 24.sp,
                                modifier = Modifier.clickable {
                                    onStarMessage(msg.id)
                                    selectedMessageForAction = null
                                }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    onReplyTo(msg)
                    selectedMessageForAction = null
                }) {
                    Text("Répondre", color = KomdeTerracotta)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    onDeleteMessage(msg.id)
                    selectedMessageForAction = null
                }) {
                    Text("Supprimer", color = Color.Red)
                }
            }
        )
    }

    // WebRTC Calls Dialog Roadmap
    showCallDialog?.let { callType ->
        AlertDialog(
            onDismissRequest = { showCallDialog = null },
            icon = {
                Icon(
                    imageVector = if (callType == "video") Icons.Default.Videocam else Icons.Default.Call,
                    contentDescription = null,
                    tint = KomdeTerracotta,
                    modifier = Modifier.size(36.dp)
                )
            },
            title = {
                Text(
                    text = if (callType == "video") "Appel Vidéo KOMDÉ" else "Appel Audio KOMDÉ",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "Appel en cours de préparation vers ${chat.title}...",
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = KomdeEmerald.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = "💡 Version 2 WebRTC : KOMDÉ intègre un protocole audio/vidéo basse consommation adapté aux réseaux mobiles africains (3G/4G/Fibre).",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showCallDialog = null }) {
                    Text("Fermer", color = KomdeTerracotta, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

@Composable
private fun MessageBubble(
    message: Message,
    isPlayingVoice: Boolean,
    voiceProgress: Float,
    playbackSpeed: Float,
    onTogglePlayVoice: () -> Unit,
    onCyclePlaybackSpeed: () -> Unit,
    onLongClick: () -> Unit,
    onSwipeReply: () -> Unit
) {
    val isMe = message.isFromMe

    val bubbleShape = if (isMe) {
        RoundedCornerShape(topStart = 16.dp, topEnd = 4.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
    } else {
        RoundedCornerShape(topStart = 4.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
    }

    val bubbleBg = if (isMe) {
        Brush.linearGradient(listOf(KomdeTerracotta, Color(0xFFC2410C)))
    } else {
        Brush.linearGradient(listOf(Color(0xFF334155), Color(0xFF1E293B)))
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSwipeReply() },
        contentAlignment = if (isMe) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Column(
            horizontalAlignment = if (isMe) Alignment.End else Alignment.Start,
            modifier = Modifier.widthIn(max = 310.dp)
        ) {
            Surface(
                shape = bubbleShape,
                color = Color.Transparent,
                shadowElevation = 1.5.dp,
                modifier = Modifier
                    .clip(bubbleShape)
                    .background(bubbleBg)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    // Quoted reply
                    if (message.replyToText != null) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color.Black.copy(alpha = 0.2f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 6.dp)
                        ) {
                            Text(
                                text = "↩ ${message.replyToText}",
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.8f),
                                modifier = Modifier.padding(6.dp)
                            )
                        }
                    }

                    // Sender name for group chats if not me
                    if (!isMe) {
                        Text(
                            text = message.senderName,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = KomdeGold,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }

                    when (message.type) {
                        MessageType.VOICE -> {
                            VoiceMessageCard(
                                message = message,
                                isPlaying = isPlayingVoice,
                                progress = voiceProgress,
                                speed = playbackSpeed,
                                onTogglePlay = onTogglePlayVoice,
                                onCycleSpeed = onCyclePlaybackSpeed
                            )
                        }
                        MessageType.PHOTO, MessageType.VIDEO, MessageType.DOCUMENT, MessageType.LOCATION -> {
                            MediaMessageCard(message = message)
                        }
                        MessageType.TEXT -> {
                            Text(
                                text = message.content,
                                color = Color.White,
                                fontSize = 14.sp,
                                lineHeight = 19.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    // Message Footer (Timestamp & Status ticks)
                    Row(
                        modifier = Modifier.align(Alignment.End),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (message.isStarred) {
                            Text(text = "❤️ ", fontSize = 10.sp)
                        }

                        Text(
                            text = message.formattedTime,
                            fontSize = 10.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )

                        if (isMe) {
                            Spacer(modifier = Modifier.width(4.dp))
                            when (message.status) {
                                MessageStatus.PENDING -> {
                                    Icon(
                                        imageVector = Icons.Default.Schedule,
                                        contentDescription = "En attente",
                                        tint = KomdeGold,
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                                MessageStatus.SENT -> {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Envoyé",
                                        tint = Color.White.copy(alpha = 0.7f),
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                                MessageStatus.DELIVERED -> {
                                    Row {
                                        Icon(Icons.Default.Check, contentDescription = null, tint = Color.White.copy(alpha = 0.7f), modifier = Modifier.size(11.dp))
                                        Icon(Icons.Default.Check, contentDescription = null, tint = Color.White.copy(alpha = 0.7f), modifier = Modifier.size(11.dp))
                                    }
                                }
                                MessageStatus.READ -> {
                                    Row {
                                        Icon(Icons.Default.Check, contentDescription = "Lu", tint = KomdeEmerald, modifier = Modifier.size(11.dp))
                                        Icon(Icons.Default.Check, contentDescription = null, tint = KomdeEmerald, modifier = Modifier.size(11.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AttachmentOption(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    color: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = label, fontSize = 11.sp, fontWeight = FontWeight.Medium)
    }
}
