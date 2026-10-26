package com.holianska.recipeapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
// ці імпорти IDE запропонує сама через Alt+Enter, пакет Res у вас свій
 // див. примітку нижче
import recipeapp.shared.generated.resources.Res
import recipeapp.shared.generated.resources.plus_jakarta_sans_regular
import recipeapp.shared.generated.resources.plus_jakarta_sans_medium
import recipeapp.shared.generated.resources.plus_jakarta_sans_semibold
import recipeapp.shared.generated.resources.plus_jakarta_sans_bold
import org.jetbrains.compose.resources.Font as ResFont

private val RecipeColors = lightColorScheme(
    primary = Color(0xFF0F766E),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFCCF1EC),
    onPrimaryContainer = Color(0xFF00302C),
    secondary = Color(0xFFF59E0B),
    onSecondary = Color(0xFF2B1800),
    secondaryContainer = Color(0xFFFFE9B8),
    onSecondaryContainer = Color(0xFF3D2800),
    tertiary = Color(0xFFE05A47),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFDAD4),
    onTertiaryContainer = Color(0xFF410B03),
    background = Color(0xFFF4FAF8),
    onBackground = Color(0xFF10231F),
    surface = Color.White,
    onSurface = Color(0xFF10231F),
    surfaceVariant = Color(0xFFE3EFEC),
    onSurfaceVariant = Color(0xFF3F5550),
    outline = Color(0xFFA7BDB7),
    error = Color(0xFFB3261E),
)

@Composable
private fun recipeTypography(): Typography {
    val family = FontFamily(
        ResFont(Res.font.plus_jakarta_sans_regular, FontWeight.Normal),
        ResFont(Res.font.plus_jakarta_sans_medium, FontWeight.Medium),
        ResFont(Res.font.plus_jakarta_sans_semibold, FontWeight.SemiBold),
        ResFont(Res.font.plus_jakarta_sans_bold, FontWeight.Bold),
    )
    val base = Typography()
    fun TextStyle.withFont() = copy(fontFamily = family)
    return Typography(
        displayLarge = base.displayLarge.withFont(),
        displayMedium = base.displayMedium.withFont(),
        displaySmall = base.displaySmall.withFont(),
        headlineLarge = base.headlineLarge.withFont(),
        headlineMedium = base.headlineMedium.withFont(),
        headlineSmall = base.headlineSmall.withFont(),
        titleLarge = base.titleLarge.withFont(),
        titleMedium = base.titleMedium.withFont(),
        titleSmall = base.titleSmall.withFont(),
        bodyLarge = base.bodyLarge.withFont(),
        bodyMedium = base.bodyMedium.withFont(),
        bodySmall = base.bodySmall.withFont(),
        labelLarge = base.labelLarge.withFont(),
        labelMedium = base.labelMedium.withFont(),
        labelSmall = base.labelSmall.withFont(),
    )
}

@Composable
fun RecipeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = RecipeColors,
        typography = recipeTypography(),
        content = content,
    )
}