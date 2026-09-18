package com.example.ui.screens

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.KomdeEmerald
import com.example.ui.theme.KomdeGold
import com.example.ui.theme.KomdeTerracotta

@Composable
fun AiAssistantScreen(
    onBack: () -> Unit,
    onSendToChat: (text: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTool by remember { mutableStateOf("translate") } // translate, polish, summary
    var inputText by remember { mutableStateOf("Bonjour, j'aimerais commander deux pagnes Faso Dan Fani pour la fête.") }
    var targetLanguage by remember { mutableStateOf("Mòoré 🇧🇫") }
    var generatedResult by remember {
        mutableStateOf(
            "« Ne y beeoogo ! Mam neda, m rata n dɩk Faso Dan Fani pagne a yiibu yĩng tɩ tond maan kibsre. Wãn la yaa y ligdi ? »\n\n(Traduction respectueuse en Mòoré adaptée aux commerçants de Ouagadougou)"
        )
    }

    val languages = listOf("Mòoré 🇧🇫", "Bamanankan 🇲🇱", "Hausa 🇳🇪", "Wolof 🇸🇳")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Retour",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Text(
                text = "KOMDÉ AI • Burkindi IA",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // Header Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF6B21A8), Color(0xFF1E1B4B), Color(0xFF0F172A))
                    )
                )
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(KomdeGold),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = "Assistant Africain Polyvalent",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Traduction en langues nationales, rédaction pour commerçants et synthèse vocale.",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Tool Selector Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ToolTab(
                title = "Traduire",
                icon = Icons.Default.Translate,
                isSelected = selectedTool == "translate",
                onClick = {
                    selectedTool = "translate"
                    inputText = "Bonjour, j'aimerais commander deux pagnes Faso Dan Fani pour la fête."
                    generatedResult = "« Ne y beeoogo ! Mam neda, m rata n dɩk Faso Dan Fani pagne a yiibu yĩng tɩ tond maan kibsre. Wãn la yaa y ligdi ? »"
                },
                modifier = Modifier.weight(1f)
            )
            ToolTab(
                title = "Commerce",
                icon = Icons.Default.Store,
                isSelected = selectedTool == "polish",
                onClick = {
                    selectedTool = "polish"
                    inputText = "Prix pagne 15000 dispo livraison Ouaga"
                    generatedResult = "✨ Message Commercial Optimisé :\n« Bonjour cher client ! Nos magnifiques pagnes Faso Dan Fani tissés à la main sont disponibles au prix promotionnel de 15 000 FCFA. Livraison rapide assurée partout à Ouagadougou. Répondez pour valider votre commande ! »"
                },
                modifier = Modifier.weight(1f)
            )
            ToolTab(
                title = "Résumer",
                icon = Icons.Default.RecordVoiceOver,
                isSelected = selectedTool == "summary",
                onClick = {
                    selectedTool = "summary"
                    inputText = "Discussion de groupe : Réunion fixée à 17h au SIAO pour préparer l'exposition, apportez les échantillons et la liste des prix."
                    generatedResult = "📌 Résumé Exécutif :\n• RDV : Aujourd'hui à 17h au SIAO\n• Objectif : Préparation exposition\n• À apporter : Échantillons + Grille tarifaire"
                },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Language Pill selector (if in translate mode)
        if (selectedTool == "translate") {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(text = "Langue cible :", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    languages.forEach { lang ->
                        val isSel = targetLanguage == lang
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSel) KomdeTerracotta else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.clickable {
                                targetLanguage = lang
                                generatedResult = when {
                                    lang.contains("Mòoré") -> "« Ne y beeoogo ! Mam neda, m rata n dɩk Faso Dan Fani pagne a yiibu yĩng tɩ tond maan kibsre. Wãn la yaa y ligdi ? »"
                                    lang.contains("Bamanankan") -> "« I ni ce ! N b'a fɛ k'a dɔrɔkɛ fila san Faso Dan Fani la seli kamma. Joli don ? »"
                                    lang.contains("Hausa") -> "« Sannu ! Ina so in sayi tufafin Faso Dan Fani guda biyu domin biki. Nawa ne farashin ? »"
                                    else -> "« Dalal ak jamm ! Dama bëgg jënd ñaari téere Dan Fani ngir màggal gi. Ñaata lay jar ? »"
                                }
                            }
                        ) {
                            Text(
                                text = lang,
                                color = if (isSel) Color.White else MaterialTheme.colorScheme.onSurface,
                                fontSize = 12.sp,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Input Box
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "Votre texte :",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("ai_input_text"),
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 4
                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = {
                        // Contextual generation
                        generatedResult = when (selectedTool) {
                            "translate" -> "« Ne y beeoogo ! $inputText »\n(Traduction contextualisée en $targetLanguage)"
                            "polish" -> "✨ Version Polie & Attractive :\n« $inputText »\n(Formulé chaleureusement pour vos clients sur KOMDÉ)"
                            else -> "📌 Synthèse Rapide :\n• Point clé : ${inputText.take(50)}...\n• Priorité : Haute"
                        }
                    },
                    modifier = Modifier.align(Alignment.End),
                    colors = ButtonDefaults.buttonColors(containerColor = KomdeTerracotta),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Générer avec l'IA", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Result Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(KomdeEmerald)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Résultat généré",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Row {
                        IconButton(onClick = { /* copy */ }) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "Copier", modifier = Modifier.size(18.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = generatedResult,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = { onSendToChat(generatedResult) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("ai_send_to_chat_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = KomdeEmerald),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Envoyer dans une discussion KOMDÉ", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
private fun ToolTab(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = if (isSelected) KomdeTerracotta else MaterialTheme.colorScheme.surface,
        shadowElevation = if (isSelected) 4.dp else 1.dp,
        modifier = modifier.clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
