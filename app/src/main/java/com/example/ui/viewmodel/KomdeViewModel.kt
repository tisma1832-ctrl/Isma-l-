package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.AppLanguage
import com.example.data.model.Chat
import com.example.data.model.CommunityGroup
import com.example.data.model.CommunityPoll
import com.example.data.model.DataSaverConfig
import com.example.data.model.LanguageDictionary
import com.example.data.model.LocalizedStrings
import com.example.data.model.Message
import com.example.data.model.MessageType
import com.example.data.model.NetworkMode
import com.example.data.model.PollOption
import com.example.data.model.StatusStory
import com.example.data.model.User
import com.example.data.repository.KomdeRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface KomdeScreen {
    object Welcome : KomdeScreen
    object Main : KomdeScreen
    data class ChatRoom(val chatId: String) : KomdeScreen
    object AiAssistant : KomdeScreen
    object AboutCreator : KomdeScreen
    object DataSaverSettings : KomdeScreen
}

data class KomdeUiState(
    val currentScreen: KomdeScreen = KomdeScreen.Main,
    val selectedTab: Int = 0, // 0: Discussions, 1: Communautés, 2: Statuts, 3: Contacts, 4: Profil
    val currentUser: User = User(
        id = "me",
        displayName = "Ismaël Regtoumda",
        username = "@regtoumda",
        phoneNumber = "+226 57 10 87 57",
        countryCode = "+226",
        countryFlag = "🇧🇫",
        bio = "Concepteur du projet KOMDÉ • Ouagadougou",
        isVerified = true
    ),
    val language: AppLanguage = AppLanguage.FRENCH,
    val searchQuery: String = "",
    val activeChatFilter: String = "Tous", // Tous, Non lus, Favoris, Groupes
    val dataSaver: DataSaverConfig = DataSaverConfig(),
    val isRecordingVoice: Boolean = false,
    val recordingDurationSec: Int = 0,
    val playingVoiceMessageId: String? = null,
    val playbackProgress: Float = 0f,
    val playbackSpeed: Float = 1.0f,
    val replyingToMessage: Message? = null,
    val showAttachmentMenu: Boolean = false,
    val showCreateGroupDialog: Boolean = false,
    val showStatusViewer: StatusStory? = null
)

class KomdeViewModel(private val repository: KomdeRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(KomdeUiState())
    val uiState: StateFlow<KomdeUiState> = _uiState.asStateFlow()

    private val _activeChatId = MutableStateFlow<String?>(null)

    val allChats: StateFlow<List<Chat>> = repository.allChats
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeMessages: StateFlow<List<Message>> = _activeChatId
        .flatMapLatest { id ->
            if (id == null) flowOf(emptyList()) else repository.getMessages(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Community groups with local interactive state
    private val _communities = MutableStateFlow(createInitialCommunities())
    val communities: StateFlow<List<CommunityGroup>> = _communities.asStateFlow()

    // Stories / Statuts
    private val _statuses = MutableStateFlow(createInitialStatuses())
    val statuses: StateFlow<List<StatusStory>> = _statuses.asStateFlow()

    private var voiceRecordJob: Job? = null
    private var voicePlaybackJob: Job? = null

    init {
        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }
    }

    val localizedStrings: LocalizedStrings
        get() = LanguageDictionary.getStrings(_uiState.value.language)

    fun navigateTo(screen: KomdeScreen) {
        if (screen is KomdeScreen.ChatRoom) {
            _activeChatId.value = screen.chatId
            viewModelScope.launch {
                repository.markChatAsRead(screen.chatId)
            }
        }
        _uiState.update { it.copy(currentScreen = screen) }
    }

    fun selectTab(tabIndex: Int) {
        _uiState.update { it.copy(selectedTab = tabIndex) }
    }

    fun setLanguage(lang: AppLanguage) {
        _uiState.update { it.copy(language = lang) }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun setChatFilter(filter: String) {
        _uiState.update { it.copy(activeChatFilter = filter) }
    }

    fun setNetworkMode(mode: NetworkMode) {
        _uiState.update { current ->
            current.copy(dataSaver = current.dataSaver.copy(networkMode = mode))
        }
        if (mode != NetworkMode.OFFLINE) {
            // Automatically flush pending messages
            _activeChatId.value?.let { chatId ->
                viewModelScope.launch {
                    repository.syncPendingMessages(chatId)
                }
            }
        }
    }

    fun toggleDataSaver() {
        _uiState.update { current ->
            val newState = !current.dataSaver.isEnabled
            current.copy(dataSaver = current.dataSaver.copy(isEnabled = newState))
        }
    }

    fun togglePinChat(chatId: String) {
        viewModelScope.launch {
            repository.togglePinChat(chatId)
        }
    }

    fun toggleStarMessage(messageId: String) {
        viewModelScope.launch {
            repository.toggleStarMessage(messageId)
        }
    }

    fun deleteMessage(messageId: String) {
        viewModelScope.launch {
            repository.deleteMessage(messageId)
        }
    }

    fun setReplyTo(message: Message?) {
        _uiState.update { it.copy(replyingToMessage = message) }
    }

    fun toggleAttachmentMenu() {
        _uiState.update { it.copy(showAttachmentMenu = !it.showAttachmentMenu) }
    }

    fun openStatus(story: StatusStory?) {
        _uiState.update { it.copy(showStatusViewer = story) }
    }

    fun sendTextMessage(chatId: String, content: String) {
        if (content.isBlank()) return
        val reply = _uiState.value.replyingToMessage
        val currentNetwork = _uiState.value.dataSaver.networkMode

        viewModelScope.launch {
            repository.sendMessage(
                chatId = chatId,
                content = content.trim(),
                type = MessageType.TEXT,
                replyToId = reply?.id,
                replyToText = reply?.content?.take(40),
                networkMode = currentNetwork
            )
        }
        _uiState.update { it.copy(replyingToMessage = null) }
    }

    fun startVoiceRecording() {
        _uiState.update { it.copy(isRecordingVoice = true, recordingDurationSec = 0) }
        voiceRecordJob?.cancel()
        voiceRecordJob = viewModelScope.launch {
            while (_uiState.value.isRecordingVoice) {
                delay(1000)
                _uiState.update { it.copy(recordingDurationSec = it.recordingDurationSec + 1) }
            }
        }
    }

    fun cancelVoiceRecording() {
        voiceRecordJob?.cancel()
        _uiState.update { it.copy(isRecordingVoice = false, recordingDurationSec = 0) }
    }

    fun stopAndSendVoiceRecording(chatId: String) {
        voiceRecordJob?.cancel()
        val duration = _uiState.value.recordingDurationSec.coerceAtLeast(1)
        val currentNetwork = _uiState.value.dataSaver.networkMode
        val isAggressive = _uiState.value.dataSaver.compressAudioVoiceNotes
        // In Opus 16kbps: ~2KB per second
        val estimatedSize = (duration * if (isAggressive) 2000L else 8000L)

        viewModelScope.launch {
            repository.sendMessage(
                chatId = chatId,
                content = "Note vocale (${duration}s)",
                type = MessageType.VOICE,
                mediaDurationSec = duration,
                mediaSizeBytes = estimatedSize,
                mediaFileName = "voice_${System.currentTimeMillis()}.opus",
                networkMode = currentNetwork
            )
        }
        _uiState.update {
            it.copy(
                isRecordingVoice = false,
                recordingDurationSec = 0,
                dataSaver = it.dataSaver.copy(
                    totalDataSavedMb = it.dataSaver.totalDataSavedMb + 0.35f
                )
            )
        }
    }

    fun togglePlayVoice(messageId: String, durationSec: Int = 15) {
        val currentPlaying = _uiState.value.playingVoiceMessageId
        if (currentPlaying == messageId) {
            // Pause
            voicePlaybackJob?.cancel()
            _uiState.update { it.copy(playingVoiceMessageId = null, playbackProgress = 0f) }
        } else {
            // Play
            voicePlaybackJob?.cancel()
            _uiState.update { it.copy(playingVoiceMessageId = messageId, playbackProgress = 0f) }
            voicePlaybackJob = viewModelScope.launch {
                val totalSteps = 100
                val speed = _uiState.value.playbackSpeed
                val stepDelay = ((durationSec * 1000L) / totalSteps / speed).toLong().coerceAtLeast(20)
                for (step in 1..totalSteps) {
                    delay(stepDelay)
                    _uiState.update { it.copy(playbackProgress = step / 100f) }
                }
                _uiState.update { it.copy(playingVoiceMessageId = null, playbackProgress = 0f) }
            }
        }
    }

    fun cyclePlaybackSpeed() {
        val speeds = listOf(1.0f, 1.5f, 2.0f)
        val currentIndex = speeds.indexOf(_uiState.value.playbackSpeed)
        val nextSpeed = speeds[(currentIndex + 1) % speeds.size]
        _uiState.update { it.copy(playbackSpeed = nextSpeed) }
    }

    fun sendSamplePhoto(chatId: String, caption: String = "Photo prise à Ouagadougou") {
        val currentNetwork = _uiState.value.dataSaver.networkMode
        val isAggressive = _uiState.value.dataSaver.compressPhotosAggressively
        val sizeBytes = if (isAggressive) 120_000L else 1_400_000L // 120KB vs 1.4MB

        viewModelScope.launch {
            repository.sendMessage(
                chatId = chatId,
                content = caption,
                type = MessageType.PHOTO,
                mediaSizeBytes = sizeBytes,
                mediaFileName = "photo_ouaga.jpg",
                networkMode = currentNetwork
            )
        }
        _uiState.update {
            it.copy(
                showAttachmentMenu = false,
                dataSaver = it.dataSaver.copy(
                    totalDataSavedMb = it.dataSaver.totalDataSavedMb + 1.28f
                )
            )
        }
    }

    fun sendSampleDocument(chatId: String) {
        val currentNetwork = _uiState.value.dataSaver.networkMode
        viewModelScope.launch {
            repository.sendMessage(
                chatId = chatId,
                content = "Guide_Technique_KOMDE_2026.pdf",
                type = MessageType.DOCUMENT,
                mediaSizeBytes = 340_000L,
                mediaFileName = "Guide_KOMDE_Afrique.pdf",
                networkMode = currentNetwork
            )
        }
        _uiState.update { it.copy(showAttachmentMenu = false) }
    }

    fun sendSampleLocation(chatId: String) {
        val currentNetwork = _uiState.value.dataSaver.networkMode
        viewModelScope.launch {
            repository.sendMessage(
                chatId = chatId,
                content = "Place de la Nation, Ouagadougou, Burkina Faso",
                type = MessageType.LOCATION,
                networkMode = currentNetwork
            )
        }
        _uiState.update { it.copy(showAttachmentMenu = false) }
    }

    fun votePoll(communityId: String, optionId: String) {
        _communities.update { list ->
            list.map { group ->
                if (group.id == communityId && group.activePoll != null) {
                    val currentPoll = group.activePoll
                    if (currentPoll.hasVoted) group
                    else {
                        val updatedOptions = currentPoll.options.map { opt ->
                            if (opt.id == optionId) opt.copy(votes = opt.votes + 1, isSelectedByUser = true)
                            else opt
                        }
                        group.copy(
                            activePoll = currentPoll.copy(
                                options = updatedOptions,
                                totalVotes = currentPoll.totalVotes + 1,
                                hasVoted = true
                            )
                        )
                    }
                } else group
            }
        }
    }

    fun updateProfile(name: String, username: String, bio: String) {
        _uiState.update {
            it.copy(
                currentUser = it.currentUser.copy(
                    displayName = name,
                    username = if (username.startsWith("@")) username else "@$username",
                    bio = bio
                )
            )
        }
    }

    private fun createInitialCommunities(): List<CommunityGroup> {
        return listOf(
            CommunityGroup(
                id = "comm_burkina_tech",
                name = "Burkina Tech & Innovation 🇧🇫",
                description = "Cercle des développeurs, entrepreneurs et innovateurs numériques du Burkina Faso.",
                membersCount = 328,
                isPrivate = false,
                iconEmoji = "💻",
                category = "Technologie & Dev",
                location = "Ouagadougou & Bobo",
                activePoll = CommunityPoll(
                    id = "poll_1",
                    question = "Quel est selon vous le plus grand défi pour les apps mobiles au Burkina ?",
                    options = listOf(
                        PollOption("opt_1", "Coût et instabilité de la connexion Internet", 184),
                        PollOption("opt_2", "Smartphones avec stockage limité", 89),
                        PollOption("opt_3", "Intégration fluide du Mobile Money", 142)
                    ),
                    totalVotes = 415,
                    hasVoted = false
                )
            ),
            CommunityGroup(
                id = "comm_dan_fani",
                name = "Artisans & Tissage Faso Dan Fani",
                description = "Valorisation du pagne traditionnel tissé main, partenariats et commandes directes.",
                membersCount = 94,
                isPrivate = false,
                iconEmoji = "🧵",
                category = "Artisanat & Commerce",
                location = "Centre Artisanal de Ouaga"
            ),
            CommunityGroup(
                id = "comm_universite",
                name = "Étudiants Joseph Ki-Zerbo (UJKZ)",
                description = "Entraide académique, polycopiés et opportunités de stages.",
                membersCount = 512,
                isPrivate = false,
                iconEmoji = "📚",
                category = "Éducation",
                location = "Ouagadougou"
            ),
            CommunityGroup(
                id = "comm_famille",
                name = "Famille Élargie Regtoumda",
                description = "Nouvelles familiales, salutations et événements du village.",
                membersCount = 24,
                isPrivate = true,
                iconEmoji = "🏡",
                category = "Famille",
                location = "Kadiogo"
            )
        )
    }

    private fun createInitialStatuses(): List<StatusStory> {
        return listOf(
            StatusStory(
                id = "stat_1",
                userId = "me",
                userName = "Moi (Ismaël Regtoumda)",
                textCaption = "Fier de lancer KOMDÉ ! Une application pensée avec fierté pour le Burkina Faso et tout le continent africain 🇧🇫🌍",
                backgroundColorHex = 0xFFD9531E,
                viewsCount = 47
            ),
            StatusStory(
                id = "stat_2",
                userId = "user_fatou",
                userName = "Fatoumata Traoré",
                textCaption = "Ne y beeoogo à tous ! Magnifique lever de soleil sur Ouaga 2000 ✨",
                backgroundColorHex = 0xFF0D9488,
                viewsCount = 89
            ),
            StatusStory(
                id = "stat_3",
                userId = "user_aminata",
                userName = "Aminata Kaboré",
                textCaption = "Nouvelle collection Dan Fani disponible à l'atelier de Bobo 👗🧵",
                backgroundColorHex = 0xFFB45309,
                viewsCount = 112
            )
        )
    }
}

class KomdeViewModelFactory(private val repository: KomdeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(KomdeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return KomdeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
