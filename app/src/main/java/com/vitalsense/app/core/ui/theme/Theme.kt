package com.vitalsense.app.core.ui.theme

import android.app.Activity
import android.content.res.Configuration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import java.util.Locale

/**
 * VitalSense uses a single, fixed light color scheme intentionally.
 *
 * Rationale: this is a field/clinical tool used by ASHA workers and
 * patients, often in bright outdoor daylight on low-end devices.
 * Dynamic color (Material You) and dark theme are deliberately NOT
 * wired here — a consistent, predictable, high-contrast light UI is
 * more legible outdoors and easier to support across roles than a
 * theme that changes per device/wallpaper.
 */
private val VSLightColorScheme = lightColorScheme(
    primary = VS_Primary,
    onPrimary = VS_OnPrimary,
    primaryContainer = VS_PrimaryContainer,
    onPrimaryContainer = VS_OnPrimaryContainer,

    secondary = VS_Secondary,
    onSecondary = VS_OnSecondary,
    secondaryContainer = VS_SecondaryContainer,
    onSecondaryContainer = VS_OnSecondaryContainer,

    tertiary = VS_Tertiary,
    onTertiary = VS_OnTertiary,
    tertiaryContainer = VS_TertiaryContainer,
    onTertiaryContainer = VS_OnTertiaryContainer,

    background = VS_Background,
    onBackground = VS_OnBackground,

    surface = VS_Surface,
    onSurface = VS_OnSurface,
    surfaceVariant = VS_SurfaceVariant,
    onSurfaceVariant = VS_OnSurfaceVariant,

    outline = VS_Outline,
    outlineVariant = VS_OutlineVariant,

    error = VS_Error,
    onError = Color(0xFFFFFFFF),
    errorContainer = VS_ErrorContainer,
    onErrorContainer = VS_OnErrorContainer
)

@Composable
fun VitalSenseTheme(
    language: AppLanguage = AppLanguage.ENGLISH,
    usePatientLightMode: Boolean = true, // Kept for compatibility but always uses VSLightColorScheme
    content: @Composable () -> Unit
) {
    val colorScheme = VSLightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = true
                isAppearanceLightNavigationBars = true
            }
        }
    }

    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val locale = remember(language) { Locale(language.code) }
    val localizedConfiguration = remember(configuration, locale) {
        Configuration(configuration).apply {
            setLocale(locale)
            setLayoutDirection(locale)
        }
    }
    val localizedContext = remember(context, localizedConfiguration) {
        context.createConfigurationContext(localizedConfiguration)
    }

    CompositionLocalProvider(
        LocalConfiguration provides localizedConfiguration,
        LocalContext provides localizedContext,
        LocalSpacing provides VitalSenseSpacing(),
        LocalAppStrings provides AppLanguageManager.getStrings(language)
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = VSTypography,
            shapes = VSShapes,
            content = content
        )
    }
}
