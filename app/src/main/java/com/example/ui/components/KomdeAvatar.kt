package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.KomdeEmerald
import com.example.ui.theme.KomdeGold
import com.example.ui.theme.KomdeTerracotta

@Composable
fun KomdeAvatar(
    name: String,
    modifier: Modifier = Modifier,
    size: Dp = 50.dp,
    isGroup: Boolean = false,
    isAiAssistant: Boolean = false,
    isOnline: Boolean = false,
    showBorder: Boolean = true
) {
    val initials = name.split(" ")
        .mapNotNull { it.firstOrNull()?.toString() }
        .take(2)
        .joinToString("")
        .uppercase()

    val gradientBrush = when {
        isAiAssistant -> Brush.linearGradient(
            listOf(Color(0xFF8B5CF6), Color(0xFF3B82F6), KomdeGold)
        )
        isGroup -> Brush.linearGradient(
            listOf(Color(0xFF0F766E), Color(0xFF14B8A6))
        )
        name.contains("Ismaël", ignoreCase = true) || name.contains("REGTOUMDA", ignoreCase = true) -> Brush.linearGradient(
            listOf(KomdeTerracotta, KomdeGold)
        )
        else -> Brush.linearGradient(
            listOf(Color(0xFF334155), Color(0xFF1E293B))
        )
    }

    Box(
        modifier = modifier
            .size(size)
            .testTag("avatar_${name.take(6)}")
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(gradientBrush)
                .then(
                    if (showBorder) Modifier.border(1.5.dp, MaterialTheme.colorScheme.surface, CircleShape)
                    else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            when {
                isAiAssistant -> {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "KOMDÉ AI",
                        tint = Color.White,
                        modifier = Modifier.size(size * 0.5f)
                    )
                }
                isGroup -> {
                    Icon(
                        imageVector = Icons.Default.Group,
                        contentDescription = "Groupe",
                        tint = Color.White,
                        modifier = Modifier.size(size * 0.5f)
                    )
                }
                initials.isNotEmpty() -> {
                    Text(
                        text = initials,
                        color = Color.White,
                        fontSize = (size.value * 0.36f).sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                else -> {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Utilisateur",
                        tint = Color.White,
                        modifier = Modifier.size(size * 0.5f)
                    )
                }
            }
        }

        if (isOnline && !isGroup) {
            Box(
                modifier = Modifier
                    .size(size * 0.28f)
                    .align(Alignment.BottomEnd)
                    .clip(CircleShape)
                    .background(KomdeEmerald)
                    .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape)
            )
        }
    }
}
