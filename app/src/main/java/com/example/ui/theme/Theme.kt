package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
  darkColorScheme(
    primary = Color(0xFFFF7A00), // Energetic Active Solar Orange Accent
    secondary = Color(0xFFE65100), // Deep Burned Orange Secondary
    tertiary = Color(0xFFFFB088), // Warm Peach Tertiary Accent
    background = Color(0xFF070402), // Pure Pitch Black with Espresso Glow
    surface = Color(0xFF1A1009), // Deep Charcoal-Orange Card Surfaces
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFFFFF5F0), // Off-White Warm Primary Text
    onSurface = Color(0xFFFFF5F0) // Off-White Warm Primary Text
  )

private val LightColorScheme =
  lightColorScheme(
    primary = Color(0xFFFF6F00), // Vibrant Deep Hot Sun Orange Accent
    secondary = Color(0xFFE65100), // Rich Burned Orange Secondary
    tertiary = Color(0xFFFF9E7D), // Vibrant Coral Peach Accent
    background = Color(0xFFFFFDFB), // Sand Pure White High-Contrast Background
    surface = Color(0xFFFFF3EC), // Cream Light Orange card surfaces
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFF2E190A), // Espresso Dark Readable Text
    onSurface = Color(0xFF2E190A) // Espresso Dark Readable Text
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Dynamic color override disabled to ensure custom brand colors are active
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
