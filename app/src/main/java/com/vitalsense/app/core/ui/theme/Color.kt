package com.vitalsense.app.core.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * VitalSense Design System — Color Tokens
 *
 * Direction: calm, trustworthy, government/public-health register.
 * - Desaturated blue-teal as primary (not vibrant SaaS blue)
 * - Muted sage green as secondary (health / ASHA / wellness)
 * - Near-white background, not pure white panels stacked on pure white
 * - Text is near-black, never pure #000000 (softer, less harsh)
 * - Status colors are muted, not neon — meant to inform, not decorate
 */

// ---------- Brand ----------
val VS_Primary = Color(0xFF2E6F8E)          // muted blue-teal
val VS_OnPrimary = Color(0xFFFFFFFF)
val VS_PrimaryContainer = Color(0xFFDCEEF5)
val VS_OnPrimaryContainer = Color(0xFF0F3644)

val VS_Secondary = Color(0xFF4F7D5C)        // muted sage green
val VS_OnSecondary = Color(0xFFFFFFFF)
val VS_SecondaryContainer = Color(0xFFE1EFE4)
val VS_OnSecondaryContainer = Color(0xFF1E3626)

val VS_Tertiary = Color(0xFF8A6D3B)         // muted gold — reserve for admin/highlights only
val VS_OnTertiary = Color(0xFFFFFFFF)
val VS_TertiaryContainer = Color(0xFFF3E6CE)
val VS_OnTertiaryContainer = Color(0xFF3A2C0F)

// ---------- Neutrals ----------
val VS_Background = Color(0xFFF7F8FA)       // soft off-white, not stark white
val VS_OnBackground = Color(0xFF1C1F23)     // near-black, softer than pure black

val VS_Surface = Color(0xFFFFFFFF)
val VS_OnSurface = Color(0xFF1C1F23)
val VS_SurfaceVariant = Color(0xFFEEF1F4)   // subtle panel differentiation
val VS_OnSurfaceVariant = Color(0xFF5B6470) // secondary text / inactive icons

val VS_Outline = Color(0xFFC7CDD4)          // borders, dividers
val VS_OutlineVariant = Color(0xFFE3E7EB)

// ---------- Status (muted, semantic only — never decorative) ----------
val VS_Success = Color(0xFF2F9E5B)
val VS_SuccessContainer = Color(0xFFE1F3E7)
val VS_OnSuccessContainer = Color(0xFF16442A)

val VS_Warning = Color(0xFFC77700)
val VS_WarningContainer = Color(0xFFF7E9D2)
val VS_OnWarningContainer = Color(0xFF4A2E00)

val VS_Error = Color(0xFFB3261E)
val VS_ErrorContainer = Color(0xFFF9DEDC)
val VS_OnErrorContainer = Color(0xFF410E0B)

val VS_Info = VS_Primary
val VS_InfoContainer = VS_PrimaryContainer

// ---------- Pending / neutral state (e.g. "Out for Delivery: Pending") ----------
val VS_Pending = Color(0xFF8A8F98)
val VS_PendingContainer = Color(0xFFEDEEF0)


val NagarSevaStatusNormalBg = Color(0xFFE1F3E7)
val NagarSevaStatusProgressBg = Color(0xFFF7E9D2)
val NagarSevaStatusUrgentBg = Color(0xFFF9DEDC)

