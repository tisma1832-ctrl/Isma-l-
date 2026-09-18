package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.KomdeDatabase
import com.example.data.model.StatusStory
import com.example.data.repository.KomdeRepository
import com.example.ui.components.KomdeAvatar
import com.example.ui.screens.AboutScreen
import com.example.ui.screens.AiAssistantScreen
import com.example.ui.screens.ChatScreen
import com.example.ui.screens.DataSaverScreen
import com.example.ui.screens.MainScreen
import com.example.ui.screens.WelcomeScreen
import com.example.ui.theme.KomdeTerracotta
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.KomdeScreen
import com.example.ui.viewmodel.KomdeViewModel
import com.example.ui.viewmodel.KomdeViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = KomdeDatabase.getInstance(applicationContext)
        val repository = KomdeRepository(database.komdeDao())
        val factory = KomdeViewModelFactory(repository)

        setContent {
            MyApplicationTheme {
                val viewModel: KomdeViewModel = viewModel(factory = factory)
                KomdeAppRoot(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun KomdeAppRoot(viewModel: KomdeViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val allChats by viewModel.allChats.collectAsState()
    val activeMessages by viewModel.activeMessages.collectAsState()
    val communities by viewModel.communities.collectAsState()
    val statuses by viewModel.statuses.collectAsState()

    // Handle Android system back button
    BackHandler(enabled = uiState.currentScreen !is KomdeScreen.Main) {
        when (uiState.currentScreen) {
            is KomdeScreen.ChatRoom,
            is KomdeScreen.AiAssistant,
            is KomdeScreen.AboutCreator,
            is KomdeScreen.DataSaverSettings -> viewModel.navigateTo(KomdeScreen.Main)
            is KomdeScreen.Welcome -> {} // At welcome root
            KomdeScreen.Main -> {}
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (val screen = uiState.currentScreen) {
            is KomdeScreen.Welcome -> {
                WelcomeScreen(
                    onComplete = { name, username, _ ->
                        viewModel.updateProfile(name, username, "Disponible sur KOMDÉ")
                        viewModel.navigateTo(KomdeScreen.Main)
                    }
                )
            }
            is KomdeScreen.Main -> {
                MainScreen(
                    selectedTab = uiState.selectedTab,
                    onTabSelected = { viewModel.selectTab(it) },
                    chats = allChats,
                    communities = communities,
                    statuses = statuses,
                    currentUser = uiState.currentUser,
                    dataSaver = uiState.dataSaver,
                    activeLanguage = uiState.language,
                    localizedStrings = viewModel.localizedStrings,
                    searchQuery = uiState.searchQuery,
                    onSearchChange = { viewModel.setSearchQuery(it) },
                    activeChatFilter = uiState.activeChatFilter,
                    onChatFilterChange = { viewModel.setChatFilter(it) },
                    onOpenChat = { chatId -> viewModel.navigateTo(KomdeScreen.ChatRoom(chatId)) },
                    onOpenAiAssistant = { viewModel.navigateTo(KomdeScreen.AiAssistant) },
                    onOpenAbout = { viewModel.navigateTo(KomdeScreen.AboutCreator) },
                    onOpenDataSaver = { viewModel.navigateTo(KomdeScreen.DataSaverSettings) },
                    onNetworkModeChange = { viewModel.setNetworkMode(it) },
                    onSetLanguage = { viewModel.setLanguage(it) },
                    onVotePoll = { commId, optId -> viewModel.votePoll(commId, optId) },
                    onOpenStatusViewer = { viewModel.openStatus(it) },
                    onUpdateProfile = { name, uname, bio -> viewModel.updateProfile(name, uname, bio) }
                )
            }
            is KomdeScreen.ChatRoom -> {
                val currentChat = allChats.find { it.id == screen.chatId }
                    ?: com.example.data.model.Chat(
                        id = screen.chatId,
                        title = "Discussion",
                        isOnline = true
                    )

                ChatScreen(
                    chat = currentChat,
                    messages = activeMessages,
                    dataSaver = uiState.dataSaver,
                    isRecordingVoice = uiState.isRecordingVoice,
                    recordingDurationSec = uiState.recordingDurationSec,
                    playingVoiceMessageId = uiState.playingVoiceMessageId,
                    playbackProgress = uiState.playbackProgress,
                    playbackSpeed = uiState.playbackSpeed,
                    replyingTo = uiState.replyingToMessage,
                    onBack = { viewModel.navigateTo(KomdeScreen.Main) },
                    onSendMessage = { text -> viewModel.sendTextMessage(screen.chatId, text) },
                    onStartVoiceRecord = { viewModel.startVoiceRecording() },
                    onCancelVoiceRecord = { viewModel.cancelVoiceRecording() },
                    onStopAndSendVoiceRecord = { viewModel.stopAndSendVoiceRecording(screen.chatId) },
                    onTogglePlayVoice = { msgId, dur -> viewModel.togglePlayVoice(msgId, dur) },
                    onCyclePlaybackSpeed = { viewModel.cyclePlaybackSpeed() },
                    onSendSamplePhoto = { viewModel.sendSamplePhoto(screen.chatId) },
                    onSendSampleDocument = { viewModel.sendSampleDocument(screen.chatId) },
                    onSendSampleLocation = { viewModel.sendSampleLocation(screen.chatId) },
                    onReplyTo = { viewModel.setReplyTo(it) },
                    onStarMessage = { viewModel.toggleStarMessage(it) },
                    onDeleteMessage = { viewModel.deleteMessage(it) },
                    onNetworkModeChange = { viewModel.setNetworkMode(it) }
                )
            }
            is KomdeScreen.AiAssistant -> {
                AiAssistantScreen(
                    onBack = { viewModel.navigateTo(KomdeScreen.Main) },
                    onSendToChat = { generatedText ->
                        viewModel.sendTextMessage("chat_creator", generatedText)
                        viewModel.navigateTo(KomdeScreen.ChatRoom("chat_creator"))
                    }
                )
            }
            is KomdeScreen.AboutCreator -> {
                AboutScreen(
                    onBack = { viewModel.navigateTo(KomdeScreen.Main) }
                )
            }
            is KomdeScreen.DataSaverSettings -> {
                DataSaverScreen(
                    dataSaver = uiState.dataSaver,
                    onToggleEnabled = { viewModel.toggleDataSaver() },
                    onNetworkModeChange = { viewModel.setNetworkMode(it) },
                    onBack = { viewModel.navigateTo(KomdeScreen.Main) }
                )
            }
        }

        // Ephemeral Status Story Full-Screen Viewer
        uiState.showStatusViewer?.let { story ->
            StatusViewerDialog(
                story = story,
                onDismiss = { viewModel.openStatus(null) }
            )
        }
    }
}

@Composable
fun StatusViewerDialog(
    story: StatusStory,
    onDismiss: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onDismiss() },
        color = Color(story.backgroundColorHex)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                KomdeAvatar(name = story.userName, size = 44.dp)
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = story.userName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text(text = story.formattedAge, color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Fermer", tint = Color.White)
                }
            }

            // Body text
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = story.textCaption,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    lineHeight = 30.sp
                )
            }

            // Footer
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.Black.copy(alpha = 0.4f)
                ) {
                    Text(
                        text = "👁️ ${story.viewsCount} vues sur KOMDÉ",
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}
