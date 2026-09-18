package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.Chat
import com.example.data.model.CommunityGroup
import com.example.data.model.DataSaverConfig
import com.example.data.model.LocalizedStrings
import com.example.data.model.MessageStatus
import com.example.data.model.NetworkMode
import com.example.data.model.StatusStory
import com.example.data.model.User
import com.example.ui.components.KomdeAvatar
import com.example.ui.components.NetworkStatusBanner
import com.example.ui.theme.KomdeEmerald
import com.example.ui.theme.KomdeGold
import com.example.ui.theme.KomdeTerracotta

@Composable
fun MainScreen(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    chats: List<Chat>,
    communities: List<CommunityGroup>,
    statuses: List<StatusStory>,
    currentUser: User,
    dataSaver: DataSaverConfig,
    activeLanguage: AppLanguage,
    localizedStrings: LocalizedStrings,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    activeChatFilter: String,
    onChatFilterChange: (String) -> Unit,
    onOpenChat: (String) -> Unit,
    onOpenAiAssistant: () -> Unit,
    onOpenAbout: () -> Unit,
    onOpenDataSaver: () -> Unit,
    onNetworkModeChange: (NetworkMode) -> Unit,
    onSetLanguage: (AppLanguage) -> Unit,
    onVotePoll: (communityId: String, optionId: String) -> Unit,
    onOpenStatusViewer: (StatusStory) -> Unit,
    onUpdateProfile: (name: String, username: String, bio: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showEditProfileDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Network resilience & simulation banner
        NetworkStatusBanner(
            dataSaver = dataSaver,
            onNetworkModeChange = onNetworkModeChange
        )

        // Brand Header Top Bar
        Surface(
            tonalElevation = 2.dp,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    androidx.compose.foundation.Image(
                        painter = androidx.compose.ui.res.painterResource(id = com.example.R.drawable.komde_logo_black_blue_1789469418875),
                        contentDescription = "Logo KOMDÉ",
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "KOMDÉ",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                color = KomdeTerracotta,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = activeLanguage.flag,
                                fontSize = 15.sp
                            )
                        }
                        Text(
                            text = localizedStrings.appSlogan,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Quick Burkindi IA button
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF6B21A8).copy(alpha = 0.15f),
                        modifier = Modifier
                            .clickable { onOpenAiAssistant() }
                            .testTag("main_ai_button")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = "Burkindi IA", tint = Color(0xFFA855F7), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "IA", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFA855F7))
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Language switch button
                    IconButton(onClick = { showLanguageDialog = true }) {
                        Icon(Icons.Default.Language, contentDescription = "Langues", tint = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
        }

        // Search Bar (shared across discussions and contacts)
        if (selectedTab == 0 || selectedTab == 3) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Search, contentDescription = "Rechercher", tint = Color.Gray, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = onSearchChange,
                        placeholder = { Text(localizedStrings.searchPlaceholder, fontSize = 13.sp, color = Color.Gray) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("main_search_input"),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent
                        )
                    )
                }
            }
        }

        // Main Tab Content
        Box(modifier = Modifier.weight(1f)) {
            when (selectedTab) {
                0 -> DiscussionsTab(
                    chats = chats,
                    activeFilter = activeChatFilter,
                    searchQuery = searchQuery,
                    onFilterChange = onChatFilterChange,
                    onChatClick = onOpenChat,
                    onOpenAiAssistant = onOpenAiAssistant
                )
                1 -> CommunitiesTab(
                    communities = communities,
                    onVotePoll = onVotePoll,
                    onOpenGroupChat = { onOpenChat("chat_burkina_tech") }
                )
                2 -> StatusesTab(
                    statuses = statuses,
                    currentUser = currentUser,
                    onOpenStatus = onOpenStatusViewer
                )
                3 -> ContactsTab(
                    searchQuery = searchQuery,
                    onStartChat = onOpenChat
                )
                4 -> ProfileTab(
                    user = currentUser,
                    dataSaver = dataSaver,
                    activeLanguage = activeLanguage,
                    onOpenEditProfile = { showEditProfileDialog = true },
                    onOpenLanguageDialog = { showLanguageDialog = true },
                    onOpenDataSaver = onOpenDataSaver,
                    onOpenAbout = onOpenAbout
                )
            }
        }

        // Bottom Navigation Bar
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            val totalUnread = chats.sumOf { it.unreadCount }

            NavigationBarItem(
                selected = selectedTab == 0,
                onClick = { onTabSelected(0) },
                icon = {
                    Box {
                        Icon(Icons.Default.Chat, contentDescription = "Discussions")
                        if (totalUnread > 0) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .background(KomdeTerracotta),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$totalUnread",
                                    color = Color.White,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                },
                label = { Text("Messages", fontSize = 11.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = KomdeTerracotta,
                    selectedTextColor = KomdeTerracotta,
                    indicatorColor = KomdeTerracotta.copy(alpha = 0.15f)
                ),
                modifier = Modifier.testTag("tab_messages")
            )

            NavigationBarItem(
                selected = selectedTab == 1,
                onClick = { onTabSelected(1) },
                icon = { Icon(Icons.Default.Group, contentDescription = "Communautés") },
                label = { Text("Communautés", fontSize = 11.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = KomdeTerracotta,
                    selectedTextColor = KomdeTerracotta,
                    indicatorColor = KomdeTerracotta.copy(alpha = 0.15f)
                ),
                modifier = Modifier.testTag("tab_communities")
            )

            NavigationBarItem(
                selected = selectedTab == 2,
                onClick = { onTabSelected(2) },
                icon = { Icon(Icons.Default.Notifications, contentDescription = "Statuts") },
                label = { Text("Statuts", fontSize = 11.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = KomdeTerracotta,
                    selectedTextColor = KomdeTerracotta,
                    indicatorColor = KomdeTerracotta.copy(alpha = 0.15f)
                ),
                modifier = Modifier.testTag("tab_statuses")
            )

            NavigationBarItem(
                selected = selectedTab == 3,
                onClick = { onTabSelected(3) },
                icon = { Icon(Icons.Default.People, contentDescription = "Contacts") },
                label = { Text("Contacts", fontSize = 11.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = KomdeTerracotta,
                    selectedTextColor = KomdeTerracotta,
                    indicatorColor = KomdeTerracotta.copy(alpha = 0.15f)
                ),
                modifier = Modifier.testTag("tab_contacts")
            )

            NavigationBarItem(
                selected = selectedTab == 4,
                onClick = { onTabSelected(4) },
                icon = { Icon(Icons.Default.Person, contentDescription = "Profil") },
                label = { Text("Profil", fontSize = 11.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = KomdeTerracotta,
                    selectedTextColor = KomdeTerracotta,
                    indicatorColor = KomdeTerracotta.copy(alpha = 0.15f)
                ),
                modifier = Modifier.testTag("tab_profile")
            )
        }
    }

    // Language Selection Dialog
    if (showLanguageDialog) {
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = { Text("Choisir la langue d'affichage", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    AppLanguage.values().forEach { lang ->
                        val isSel = activeLanguage == lang
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSel) KomdeTerracotta.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSetLanguage(lang)
                                    showLanguageDialog = false
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = lang.flag, fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = lang.nativeName,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSel) KomdeTerracotta else MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(text = lang.displayName, fontSize = 11.sp, color = Color.Gray)
                                }
                                Spacer(modifier = Modifier.weight(1f))
                                if (isSel) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = KomdeTerracotta)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showLanguageDialog = false }) {
                    Text("Fermer", color = KomdeTerracotta)
                }
            }
        )
    }

    // Edit Profile Dialog
    if (showEditProfileDialog) {
        var editName by remember { mutableStateOf(currentUser.displayName) }
        var editUsername by remember { mutableStateOf(currentUser.username) }
        var editBio by remember { mutableStateOf(currentUser.bio) }

        AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            title = { Text("Modifier mon profil", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Nom complet") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editUsername,
                        onValueChange = { editUsername = it },
                        label = { Text("Identifiant KOMDÉ (@)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editBio,
                        onValueChange = { editBio = it },
                        label = { Text("Bio / Statut") },
                        maxLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdateProfile(editName, editUsername, editBio)
                        showEditProfileDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = KomdeTerracotta)
                ) {
                    Text("Enregistrer")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditProfileDialog = false }) {
                    Text("Annuler")
                }
            }
        )
    }
}

// ---------------------------------------------------------
// Tab 0: Discussions List
// ---------------------------------------------------------
@Composable
private fun DiscussionsTab(
    chats: List<Chat>,
    activeFilter: String,
    searchQuery: String,
    onFilterChange: (String) -> Unit,
    onChatClick: (String) -> Unit,
    onOpenAiAssistant: () -> Unit
) {
    val filters = listOf("Tous", "Non lus", "Favoris", "Groupes")

    val filteredChats = chats.filter { chat ->
        val matchesQuery = searchQuery.isBlank() ||
                chat.title.contains(searchQuery, ignoreCase = true) ||
                chat.lastMessageText.contains(searchQuery, ignoreCase = true)
        val matchesFilter = when (activeFilter) {
            "Non lus" -> chat.unreadCount > 0
            "Favoris" -> chat.isFavorite || chat.isPinned
            "Groupes" -> chat.isGroup
            else -> true
        }
        matchesQuery && matchesFilter
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Filter chips
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filters) { filter ->
                    val isSelected = activeFilter == filter
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) KomdeTerracotta else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.clickable { onFilterChange(filter) }
                    ) {
                        Text(
                            text = filter,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            // Chat items list
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredChats, key = { it.id }) { chat ->
                    ChatItemRow(chat = chat, onClick = { onChatClick(chat.id) })
                }
            }
        }

        // Floating Action Button
        FloatingActionButton(
            onClick = onOpenAiAssistant,
            containerColor = KomdeTerracotta,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .testTag("main_fab_new_chat")
        ) {
            Icon(Icons.Default.AutoAwesome, contentDescription = "Nouvelle discussion / IA", tint = Color.White)
        }
    }
}

@Composable
private fun ChatItemRow(
    chat: Chat,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .testTag("chat_item_${chat.id}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        KomdeAvatar(
            name = chat.title,
            size = 52.dp,
            isGroup = chat.isGroup,
            isAiAssistant = chat.isAiAssistant,
            isOnline = chat.isOnline
        )

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Text(
                        text = chat.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (chat.categoryBadge != null) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = KomdeTerracotta.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = chat.categoryBadge,
                                fontSize = 9.sp,
                                color = KomdeTerracotta,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                            )
                        }
                    }
                }

                Text(
                    text = chat.formattedTime,
                    fontSize = 11.sp,
                    color = if (chat.unreadCount > 0) KomdeTerracotta else Color.Gray,
                    fontWeight = if (chat.unreadCount > 0) FontWeight.Bold else FontWeight.Normal
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    if (chat.lastMessageSender == "Moi") {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    Text(
                        text = chat.lastMessageText,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (chat.isPinned) {
                        Icon(
                            imageVector = Icons.Default.PushPin,
                            contentDescription = "Épinglé",
                            tint = Color.Gray,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }

                    if (chat.unreadCount > 0) {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(KomdeTerracotta),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${chat.unreadCount}",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------
// Tab 1: Communities & Polls
// ---------------------------------------------------------
@Composable
private fun CommunitiesTab(
    communities: List<CommunityGroup>,
    onVotePoll: (communityId: String, optionId: String) -> Unit,
    onOpenGroupChat: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(KomdeTerracotta),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Group, contentDescription = null, tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Espaces Communautaires Africains", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(text = "Échangez par centres d'intérêt, commerce local et entraide citoyenne.", fontSize = 11.sp, color = Color.Gray)
                    }
                }
            }
        }

        items(communities, key = { it.id }) { group ->
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenGroupChat() }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = group.iconEmoji, fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(text = group.name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Text(text = "${group.membersCount} membres • ${group.location}", fontSize = 11.sp, color = Color.Gray)
                            }
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = KomdeTerracotta.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = group.category,
                                fontSize = 10.sp,
                                color = KomdeTerracotta,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = group.description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                    // Interactive Poll if available
                    group.activePoll?.let { poll ->
                        Spacer(modifier = Modifier.height(12.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = "📊 Sondage Communautaire", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = KomdeGold)
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(text = "${poll.totalVotes} votes", fontSize = 10.sp, color = Color.Gray)
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(text = poll.question, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                Spacer(modifier = Modifier.height(8.dp))

                                poll.options.forEach { opt ->
                                    val percent = if (poll.totalVotes > 0) (opt.votes * 100) / poll.totalVotes else 0
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (opt.isSelectedByUser) KomdeTerracotta.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface,
                                        border = if (opt.isSelectedByUser) androidx.compose.foundation.BorderStroke(1.dp, KomdeTerracotta) else null,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 3.dp)
                                            .clickable { onVotePoll(group.id, opt.id) }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(text = opt.text, fontSize = 11.sp, modifier = Modifier.weight(1f))
                                            Text(text = "$percent%", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = KomdeTerracotta)
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
}

// ---------------------------------------------------------
// Tab 2: Statuses (Stories)
// ---------------------------------------------------------
@Composable
private fun StatusesTab(
    statuses: List<StatusStory>,
    currentUser: User,
    onOpenStatus: (StatusStory) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // My Status Row
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* add status */ }
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box {
                    KomdeAvatar(name = currentUser.displayName, size = 54.dp)
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .align(Alignment.BottomEnd)
                            .clip(CircleShape)
                            .background(KomdeTerracotta),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Ajouter statut", tint = Color.White, modifier = Modifier.size(14.dp))
                    }
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(text = "Mon statut", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text(text = "Partager une photo, un mot ou une pensée", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }

        item {
            Text(
                text = "Mises à jour récentes (24h)",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        items(statuses, key = { it.id }) { story ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenStatus(story) }
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .border(2.dp, KomdeTerracotta, CircleShape)
                            .padding(2.dp)
                    ) {
                        KomdeAvatar(name = story.userName, size = 48.dp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = story.userName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(text = story.formattedAge, fontSize = 11.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = story.textCaption, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------
// Tab 3: Contacts
// ---------------------------------------------------------
@Composable
private fun ContactsTab(
    searchQuery: String,
    onStartChat: (String) -> Unit
) {
    val sampleContacts = listOf(
        User("c1", "Ismaël Regtoumda", "@regtoumda", "+226 57 10 87 57", "+226", "🇧🇫", bio = "Concepteur du projet KOMDÉ", isVerified = true, isOnline = true),
        User("c2", "Aminata Kaboré", "@aminata_k", "+226 70 20 30 40", "+226", "🇧🇫", bio = "Artisanat & Dan Fani • Bobo", isVerified = false, isOnline = true),
        User("c3", "Ousmane Diallo", "@ousmane_dev", "+226 76 11 22 33", "+226", "🇧🇫", bio = "Développeur Mobile & Kotlin", isVerified = false, isOnline = false),
        User("c4", "Fatoumata Traoré", "@fatou_ml", "+223 60 12 34 56", "+223", "🇲🇱", bio = "Entrepreneure Bamako", isVerified = false, isOnline = true),
        User("c5", "Ibrahim Souley", "@ibrahim_ne", "+227 90 44 55 66", "+227", "🇳🇪", bio = "Niamey Tech", isVerified = false, isOnline = true)
    )

    val filtered = sampleContacts.filter {
        searchQuery.isBlank() ||
                it.displayName.contains(searchQuery, ignoreCase = true) ||
                it.username.contains(searchQuery, ignoreCase = true) ||
                it.phoneNumber.contains(searchQuery)
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(filtered, key = { it.id }) { contact ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val targetId = when (contact.id) {
                            "c1" -> "chat_creator"
                            "c2" -> "chat_aminata"
                            else -> "chat_burkina_tech"
                        }
                        onStartChat(targetId)
                    }
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                KomdeAvatar(name = contact.displayName, size = 48.dp, isOnline = contact.isOnline)
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = contact.displayName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        if (contact.isVerified) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.Verified, contentDescription = null, tint = KomdeEmerald, modifier = Modifier.size(14.dp))
                        }
                    }
                    Text(text = "${contact.username} • ${contact.phoneNumber}", fontSize = 11.sp, color = Color.Gray)
                    Text(text = contact.bio, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                Button(
                    onClick = {
                        val targetId = if (contact.id == "c1") "chat_creator" else "chat_aminata"
                        onStartChat(targetId)
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = KomdeTerracotta.copy(alpha = 0.15f)),
                    elevation = null
                ) {
                    Text("Discuter", color = KomdeTerracotta, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ---------------------------------------------------------
// Tab 4: Profile & Settings
// ---------------------------------------------------------
@Composable
private fun ProfileTab(
    user: User,
    dataSaver: DataSaverConfig,
    activeLanguage: AppLanguage,
    onOpenEditProfile: () -> Unit,
    onOpenLanguageDialog: () -> Unit,
    onOpenDataSaver: () -> Unit,
    onOpenAbout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // User Profile Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(3.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    KomdeAvatar(name = user.displayName, size = 64.dp, isOnline = true)
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = user.displayName, fontWeight = FontWeight.Black, fontSize = 17.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(Icons.Default.Verified, contentDescription = "Vérifié", tint = KomdeEmerald, modifier = Modifier.size(16.dp))
                        }
                        Text(text = user.username, color = KomdeTerracotta, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text(text = user.phoneNumber, color = Color.Gray, fontSize = 12.sp)
                    }
                    IconButton(onClick = onOpenEditProfile) {
                        Icon(Icons.Default.Edit, contentDescription = "Modifier", tint = KomdeTerracotta)
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "« ${user.bio} »",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
        }

        // Data Saver Shortcut Banner
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onOpenDataSaver() }
                .testTag("profile_data_saver_card")
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF0284C7)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Bolt, contentDescription = null, tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = "Mode Économie de Données", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(text = "${dataSaver.totalDataSavedMb.toInt()} Mo préservés sur votre forfait", color = Color(0xFF38BDF8), fontSize = 12.sp)
                    }
                }
                Text(text = "Gérer →", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        // Settings group
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(vertical = 6.dp)) {
                SettingsItem(
                    icon = Icons.Default.Language,
                    title = "Langue de l'application",
                    subtitle = "${activeLanguage.displayName} (${activeLanguage.nativeName})",
                    onClick = onOpenLanguageDialog
                )
                SettingsItem(
                    icon = Icons.Default.Security,
                    title = "Sécurité & Confidentialité",
                    subtitle = "Chiffrement local, code PIN, sessions",
                    onClick = { /* Security */ }
                )
                SettingsItem(
                    icon = Icons.Default.Storage,
                    title = "Stockage et cache local",
                    subtitle = "Optimisation de la mémoire du téléphone",
                    onClick = onOpenDataSaver
                )
                SettingsItem(
                    icon = Icons.Default.Info,
                    title = "À propos de KOMDÉ",
                    subtitle = "ISMAËL REGTOUMDA • Vision africaine",
                    onClick = onOpenAbout,
                    highlight = true
                )
            }
        }

        Text(
            text = "KOMDÉ v1.0.0 (MVP) • Ouagadougou, Burkina Faso",
            fontSize = 11.sp,
            color = Color.Gray,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
private fun SettingsItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    highlight: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(if (highlight) KomdeTerracotta.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (highlight) KomdeTerracotta else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontWeight = if (highlight) FontWeight.Bold else FontWeight.Medium,
                fontSize = 14.sp,
                color = if (highlight) KomdeTerracotta else MaterialTheme.colorScheme.onSurface
            )
            Text(text = subtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Text(text = "›", fontSize = 18.sp, color = Color.Gray)
    }
}
