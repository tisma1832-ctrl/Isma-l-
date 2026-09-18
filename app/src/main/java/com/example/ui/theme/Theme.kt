package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
  primary = KomdeTerracottaLight,
  onPrimary = Color.White,
  primaryContainer = KomdeTerracottaDark,
  onPrimaryContainer = Color(0xFFFFDBCF),
  secondary = KomdeGold,
  onSecondary = Color.Black,
  secondaryContainer = KomdeSlateMedium,
  onSecondaryContainer = KomdeGoldLight,
  tertiary = KomdeTeal,
  onTertiary = Color.White,
  background = KomdeDarkBackground,
  onBackground = Color(0xFFF1F5F9),
  surface = KomdeDarkSurface,
  onSurface = Color(0xFFF1F5F9),
  surfaceVariant = KomdeDarkCard,
  onSurfaceVariant = Color(0xFFCBD5E1),
  outline = KomdeDarkCardBorder
)

private val LightColorScheme = lightColorScheme(
  primary = KomdeTerracotta,
  onPrimary = Color.White,
  primaryContainer = Color(0xFFFFECE5),
  onPrimaryContainer = KomdeTerracottaDark,
  secondary = KomdeSlateDark,
  onSecondary = Color.White,
  secondaryContainer = KomdeGoldContainer,
  onSecondaryContainer = Color(0xFF78350F),
  tertiary = KomdeTeal,
  onTertiary = Color.White,
  background = KomdeSandLight,
  onBackground = Color(0xFF0F172A),
  surface = KomdeSurfaceWhite,
  onSurface = Color(0xFF0F172A),
  surfaceVariant = Color(0xFFF5F0E8),
  onSurfaceVariant = Color(0xFF475569),
  outline = KomdeCardBorder
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Use our handcrafted African brand palette by default
  content: @Composable () -> Unit,
) {
  val colorScheme = when {
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      val context = LocalContext.current
      if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }
    darkTheme -> DarkColorScheme
    else -> LightColorScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}

