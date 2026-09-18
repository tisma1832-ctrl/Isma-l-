package com.example.data.model

data class CommunityGroup(
    val id: String,
    val name: String,
    val description: String,
    val membersCount: Int,
    val isPrivate: Boolean,
    val iconEmoji: String,
    val category: String, // Tech, Famille, Commerce, Culture, Entraide
    val location: String = "Burkina Faso",
    val activePoll: CommunityPoll? = null
)

data class CommunityPoll(
    val id: String,
    val question: String,
    val options: List<PollOption>,
    val totalVotes: Int,
    val hasVoted: Boolean = false
)

data class PollOption(
    val id: String,
    val text: String,
    val votes: Int,
    val isSelectedByUser: Boolean = false
)
