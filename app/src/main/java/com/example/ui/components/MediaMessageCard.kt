package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Message
import com.example.data.model.MessageType
import com.example.ui.theme.KomdeEmerald
import com.example.ui.theme.KomdeGold
import com.example.ui.theme.KomdeTerracotta

@Composable
fun MediaMessageCard(
    message: Message,
    modifier: Modifier = Modifier
) {
    when (message.type) {
        MessageType.PHOTO -> {
            Column(modifier = modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.komde_hero_welcome_1789466460352),
                        contentDescription = "Photo reçue",
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )

                    // Data saving tag on photo
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.Black.copy(alpha = 0.65f),
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "⚡ Optimisé KOMDÉ • ${message.formattedSize.ifEmpty { "120 KB" }}",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }
                if (message.content.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = message.content,
                        fontSize = 13.sp,
                        color = if (message.isFromMe) Color.White else Color.Black
                    )
                }
            }
        }
        MessageType.VIDEO -> {
            Column(modifier = modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF1E293B)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayCircle,
                        contentDescription = "Lire vidéo",
                        tint = Color.White,
                        modifier = Modifier.size(54.dp)
                    )
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.Black.copy(alpha = 0.6f),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "Max 3 min • ${message.formattedSize.ifEmpty { "4.2 MB" }}",
                            color = Color.White,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                if (message.content.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = message.content, fontSize = 13.sp)
                }
            }
        }
        MessageType.DOCUMENT -> {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = if (message.isFromMe) Color.White.copy(alpha = 0.15f) else Color.Black.copy(alpha = 0.05f),
                modifier = modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFDC2626)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = "Document",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = message.mediaFileName ?: message.content,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (message.isFromMe) Color.White else Color.Black,
                            maxLines = 1
                        )
                        Text(
                            text = "PDF • ${message.formattedSize.ifEmpty { "340 KB" }}",
                            fontSize = 10.sp,
                            color = if (message.isFromMe) Color.White.copy(alpha = 0.7f) else Color.Gray
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = "Télécharger",
                        tint = if (message.isFromMe) Color.White else KomdeTerracotta,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
        MessageType.LOCATION -> {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = if (message.isFromMe) Color.White.copy(alpha = 0.15f) else Color.Black.copy(alpha = 0.05f),
                modifier = modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(KomdeEmerald),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Localisation",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Position partagée",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (message.isFromMe) Color.White else Color.Black
                        )
                        Text(
                            text = message.content,
                            fontSize = 12.sp,
                            color = if (message.isFromMe) Color.White.copy(alpha = 0.8f) else Color.DarkGray
                        )
                    }
                }
            }
        }
        else -> {}
    }
}
