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
    primary = Color(0xFF5A7CF1), // Energetic Royal/Cornflower Blue
    secondary = Color(0xFF34D399), // Mint Green
    tertiary = Color(0xFFFB923C), // Accent Orange
    background = Color(0xFF0E1428), // Deep Ocean Cobalt
    surface = Color(0xFF182246), // Cobalt Blue Surface
    onPrimary = Color.White,
    onSecondary = Color(0xFF0E1428),
    onBackground = Color(0xFFF8FAFC),
    onSurface = Color(0xFFF8FAFC)
  )

private val LightColorScheme =
  lightColorScheme(
    primary = Color(0xFF4F70FA), // Warm Royal/Indigo Blue
    secondary = Color(0xFF10B981), // Emerald Teal
    tertiary = Color(0xFFF97316), // Accent Amber/Coral
    background = Color(0xFFF3F7FC), // Clean Soft Light Blue-Grey Background
    surface = Color(0xFFFFFFFF), // Pure White Rounded Cards
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFF1E293B), // Deep Slate-Blue Text
    onSurface = Color(0xFF1E293B)
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
