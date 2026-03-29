package com.impulsfp.mobile.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlue,
    secondary = SecondaryBlue,
    tertiary = SecondaryBlue,
    background = TextPrimary,
    surface = TextPrimary,
    onPrimary = OnPrimary,
    onSecondary = OnPrimary,
    onTertiary = OnPrimary,
    onBackground = OnPrimary,
    onSurface = OnPrimary
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    secondary = SecondaryBlue,
    tertiary = SecondaryBlue,
    background = Background,
    surface = LightBlue,
    onPrimary = OnPrimary,
    onSecondary = OnPrimary,
    onTertiary = OnPrimary,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

@Composable
fun ImpulsFPTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) {
                dynamicDarkColorScheme(context)
            } else {
                dynamicLightColorScheme(context)
            }
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