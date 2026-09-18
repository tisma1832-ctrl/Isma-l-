package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.KomdeAvatar
import com.example.ui.theme.KomdeEmerald
import com.example.ui.theme.KomdeGold
import com.example.ui.theme.KomdeTerracotta

data class CountryItem(val code: String, val dial: String, val name: String, val flag: String)

@Composable
fun WelcomeScreen(
    onComplete: (name: String, username: String, phone: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var step by remember { mutableIntStateOf(0) } // 0: Landing, 1: Phone, 2: OTP, 3: Profile

    val countries = listOf(
        CountryItem("BF", "+226", "Burkina Faso", "🇧🇫"),
        CountryItem("CI", "+225", "Côte d'Ivoire", "🇨🇮"),
        CountryItem("ML", "+223", "Mali", "🇲🇱"),
        CountryItem("SN", "+221", "Sénégal", "🇸🇳"),
        CountryItem("NE", "+227", "Niger", "🇳🇪"),
        CountryItem("TG", "+228", "Togo", "🇹🇬"),
        CountryItem("BJ", "+229", "Bénin", "🇧🇯")
    )
    var selectedCountry by remember { mutableStateOf(countries[0]) }
    var phoneNumber by remember { mutableStateOf("57 10 87 57") }
    var otpCode by remember { mutableStateOf("2026") }
    var fullName by remember { mutableStateOf("Ismaël Regtoumda") }
    var username by remember { mutableStateOf("regtoumda") }
    var bio by remember { mutableStateOf("Concepteur du projet KOMDÉ • Ouagadougou") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF0F172A),
                        Color(0xFF1E293B),
                        Color(0xFF2E170E)
                    )
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        AnimatedContent(
            targetState = step,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "welcome_step_anim"
        ) { currentStep ->
            when (currentStep) {
                0 -> LandingStep(
                    onStart = { step = 1 }
                )
                1 -> PhoneStep(
                    countries = countries,
                    selectedCountry = selectedCountry,
                    onSelectCountry = { selectedCountry = it },
                    phoneNumber = phoneNumber,
                    onPhoneChange = { phoneNumber = it },
                    onNext = { step = 2 },
                    onBack = { step = 0 }
                )
                2 -> OtpStep(
                    phone = "${selectedCountry.dial} $phoneNumber",
                    otpCode = otpCode,
                    onOtpChange = { otpCode = it },
                    onVerify = { step = 3 },
                    onBack = { step = 1 }
                )
                3 -> ProfileStep(
                    fullName = fullName,
                    onNameChange = {
                        fullName = it
                        if (username.isEmpty() || username == "regtoumda") {
                            username = it.lowercase().replace(" ", "_").take(15)
                        }
                    },
                    username = username,
                    onUsernameChange = { username = it },
                    bio = bio,
                    onBioChange = { bio = it },
                    onFinish = {
                        onComplete(fullName, "@$username", "${selectedCountry.dial} $phoneNumber")
                    }
                )
            }
        }
    }
}

@Composable
private fun LandingStep(onStart: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // App Logo & Artwork
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Card(
                shape = RoundedCornerShape(28.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier
                    .size(110.dp)
                    .clip(RoundedCornerShape(28.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.komde_logo_black_blue_1789469418875),
                    contentDescription = "KOMDÉ Logo",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "KOMDÉ",
                fontSize = 38.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                letterSpacing = 2.sp
            )

            Text(
                text = "Communiquer • Partager • Connecter",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = KomdeGold,
                textAlign = TextAlign.Center
            )
        }

        // Hero Illustration Card
        Card(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .padding(vertical = 12.dp),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.komde_hero_welcome_1789466460352),
                contentDescription = "Connexion africaine",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Feature Highlights
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            FeaturePill(icon = "⚡", title = "Léger & Ultra-Rapide", desc = "Faible consommation de batterie et de RAM")
            FeaturePill(icon = "📉", title = "Mode Économie de données", desc = "Compression poussée pour préserver vos forfaits")
            FeaturePill(icon = "🕒", title = "Mode Hors-Connexion", desc = "Envoi automatique dès retour du réseau")
            FeaturePill(icon = "🌍", title = "Langues Nationales & IA", desc = "Mòoré, Bambara, Hausa et Burkindi IA")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onStart,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("welcome_start_button"),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = KomdeTerracotta)
        ) {
            Text(
                text = "Commencer l'expérience",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color.White)
        }

        Text(
            text = "Conçu à Ouagadougou par ISMAËL REGTOUMDA",
            fontSize = 11.sp,
            color = Color.White.copy(alpha = 0.6f),
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
private fun FeaturePill(icon: String, title: String, desc: String) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.White.copy(alpha = 0.08f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = icon, fontSize = 20.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = title, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                Text(text = desc, color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
            }
        }
    }
}

@Composable
private fun PhoneStep(
    countries: List<CountryItem>,
    selectedCountry: CountryItem,
    onSelectCountry: (CountryItem) -> Unit,
    phoneNumber: String,
    onPhoneChange: (String) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    var expandedCountry by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            TextButton(onClick = onBack) {
                Text("← Retour", color = KomdeGold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Entrez votre numéro",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "KOMDÉ utilise votre numéro pour vous connecter en toute sécurité.",
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
            )

            // Country Selector Dropdown Card
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expandedCountry = !expandedCountry }
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = selectedCountry.flag, fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = selectedCountry.name, color = Color.White, fontWeight = FontWeight.Medium)
                            Text(text = "Indicatif ${selectedCountry.dial}", color = KomdeGold, fontSize = 12.sp)
                        }
                    }
                    Text(text = if (expandedCountry) "▲" else "▼", color = Color.White.copy(alpha = 0.6f))
                }
            }

            if (expandedCountry) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF1E293B))
                ) {
                    countries.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSelectCountry(item)
                                    expandedCountry = false
                                }
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = item.flag, fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = item.name, color = Color.White, modifier = Modifier.weight(1f))
                            Text(text = item.dial, color = KomdeGold, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Phone Input
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = onPhoneChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("phone_input_field"),
                leadingIcon = {
                    Text(
                        text = selectedCountry.dial,
                        color = KomdeGold,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                },
                placeholder = { Text("57 10 87 57", color = Color.White.copy(alpha = 0.4f)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Shield, contentDescription = null, tint = KomdeEmerald, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Chiffrement et protection des données respectés.",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 11.sp
                )
            }
        }

        Button(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("send_otp_button"),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = KomdeTerracotta)
        ) {
            Text("Recevoir le code OTP", fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun OtpStep(
    phone: String,
    otpCode: String,
    onOtpChange: (String) -> Unit,
    onVerify: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            TextButton(onClick = onBack) {
                Text("← Modifier le numéro", color = KomdeGold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Vérification du code",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Un code de confirmation sécurisé a été envoyé au $phone.",
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
            )

            OutlinedTextField(
                value = otpCode,
                onValueChange = onOtpChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("otp_input_field"),
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = KomdeGold)
                },
                placeholder = { Text("Code à 4 chiffres (ex: 2026)", color = Color.White.copy(alpha = 0.4f)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = KomdeEmerald.copy(alpha = 0.15f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = KomdeEmerald, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Vérification instantanée activée pour le test.",
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Button(
            onClick = onVerify,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("verify_otp_button"),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = KomdeTerracotta)
        ) {
            Text("Vérifier le code", fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun ProfileStep(
    fullName: String,
    onNameChange: (String) -> Unit,
    username: String,
    onUsernameChange: (String) -> Unit,
    bio: String,
    onBioChange: (String) -> Unit,
    onFinish: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Créer votre profil KOMDÉ",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Choisissez votre identifiant unique @utilisateur",
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
            )

            // Avatar Preview
            KomdeAvatar(
                name = fullName.ifEmpty { "User" },
                size = 76.dp,
                isOnline = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Name Field
            OutlinedTextField(
                value = fullName,
                onValueChange = onNameChange,
                label = { Text("Nom et Prénom", color = Color.White.copy(alpha = 0.8f)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("profile_name_input"),
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null, tint = KomdeGold)
                },
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Unique Username Field
            OutlinedTextField(
                value = username,
                onValueChange = onUsernameChange,
                label = { Text("Identifiant KOMDÉ unique", color = Color.White.copy(alpha = 0.8f)) },
                prefix = { Text("@", color = KomdeTerracotta, fontWeight = FontWeight.Bold) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("profile_username_input"),
                supportingText = {
                    Text("Ex: @regtoumda • Permet de vous contacter sans dévoiler votre numéro", fontSize = 11.sp, color = Color.White.copy(alpha = 0.6f))
                },
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Bio
            OutlinedTextField(
                value = bio,
                onValueChange = onBioChange,
                label = { Text("Statut / Description", color = Color.White.copy(alpha = 0.8f)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                maxLines = 2
            )
        }

        Button(
            onClick = onFinish,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("finish_profile_button"),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = KomdeTerracotta)
        ) {
            Text("Entrer sur KOMDÉ", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}
