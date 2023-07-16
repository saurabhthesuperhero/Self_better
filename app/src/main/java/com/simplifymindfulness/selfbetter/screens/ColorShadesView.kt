package com.simplifymindfulness.selfbetter.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

@Composable
fun ColorShadesView(colorScheme: ColorScheme) {
    val colorProperties = mapOf(
        "Primary" to colorScheme.primary,
        "OnPrimary" to colorScheme.onPrimary,
        "PrimaryContainer" to colorScheme.primaryContainer,
        "OnPrimaryContainer" to colorScheme.onPrimaryContainer,
        "InversePrimary" to colorScheme.inversePrimary,
        "Secondary" to colorScheme.secondary,
        "OnSecondary" to colorScheme.onSecondary,
        "SecondaryContainer" to colorScheme.secondaryContainer,
        "OnSecondaryContainer" to colorScheme.onSecondaryContainer,
        "Tertiary" to colorScheme.tertiary,
        "OnTertiary" to colorScheme.onTertiary,
        "TertiaryContainer" to colorScheme.tertiaryContainer,
        "OnTertiaryContainer" to colorScheme.onTertiaryContainer,
        "Background" to colorScheme.background,
        "OnBackground" to colorScheme.onBackground,
        "Surface" to colorScheme.surface,
        "OnSurface" to colorScheme.onSurface,
        "SurfaceVariant" to colorScheme.surfaceVariant,
        "OnSurfaceVariant" to colorScheme.onSurfaceVariant,
        "SurfaceTint" to colorScheme.surfaceTint,
        "InverseSurface" to colorScheme.inverseSurface,
        "InverseOnSurface" to colorScheme.inverseOnSurface,
        "Error" to colorScheme.error,
        "OnError" to colorScheme.onError,
        "ErrorContainer" to colorScheme.errorContainer,
        "OnErrorContainer" to colorScheme.onErrorContainer,
        "Outline" to colorScheme.outline,
        "OutlineVariant" to colorScheme.outlineVariant,
        "Scrim" to colorScheme.scrim
    )

    LazyColumn {
        items(colorProperties.entries.toList()) { (propertyName, colorValue) ->
            colorValue?.let { color ->
                ColorShadeItem(propertyName, color)
            }
        }
    }
}