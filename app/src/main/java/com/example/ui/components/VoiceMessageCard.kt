package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Message
import com.example.ui.theme.KomdeEmerald
import com.example.ui.theme.KomdeGold
import com.example.ui.theme.KomdeTerracotta

@Composable
fun VoiceMessageCard(
    message: Message,
    isPlaying: Boolean,
    progress: Float,
    speed: Float,
    onTogglePlay: () -> Unit,
    onCycleSpeed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val durationSec = message.mediaDurationSec ?: 15
    val currentSec = (progress * durationSec).toInt()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .testTag("voice_message_card_${message.id}")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Play / Pause Circle Button
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(if (message.isFromMe) Color.White.copy(alpha = 0.25f) else KomdeTerracotta)
                    .clickable { onTogglePlay() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) "Pause" else "Lecture",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Waveform visualizer
            val waveform = if (message.voiceWaveform.isNotEmpty()) message.voiceWaveform
            else listOf(0.2f, 0.5f, 0.8f, 0.3f, 0.9f, 1.0f, 0.6f, 0.4f, 0.7f, 0.9f, 0.5f, 0.3f, 0.7f, 0.4f)

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val progressIndex = (progress * waveform.size).toInt()
                    waveform.forEachIndexed { index, heightFactor ->
                        val barHeight = (heightFactor * 22).dp.coerceAtLeast(4.dp)
                        val isPlayed = index <= progressIndex
                        val barColor = if (message.isFromMe) {
                            if (isPlayed) Color.White else Color.White.copy(alpha = 0.4f)
                        } else {
                            if (isPlayed) KomdeTerracotta else Color.Gray.copy(alpha = 0.5f)
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(barHeight)
                                .clip(RoundedCornerShape(2.dp))
                                .background(barColor)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isPlaying) String.format("%02d:%02d", currentSec / 60, currentSec % 60)
                        else String.format("%02d:%02d", durationSec / 60, durationSec % 60),
                        fontSize = 11.sp,
                        color = if (message.isFromMe) Color.White.copy(alpha = 0.85f) else Color.Gray
                    )

                    // Audio compression badge
                    Text(
                        text = "Opus 16k • ${message.formattedSize}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (message.isFromMe) Color.White.copy(alpha = 0.7f) else KomdeEmerald
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Speed multiplier button (1.0x / 1.5x / 2.0x)
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (message.isFromMe) Color.White.copy(alpha = 0.2f) else Color.Black.copy(alpha = 0.08f),
                modifier = Modifier.clickable { onCycleSpeed() }
            ) {
                Text(
                    text = "${speed}x",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (message.isFromMe) Color.White else Color.Black,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                )
            }
        }
    }
}
