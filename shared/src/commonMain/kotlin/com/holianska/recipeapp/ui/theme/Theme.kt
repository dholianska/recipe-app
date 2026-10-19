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
    primary = Color(0xFFA63A00),            // темна "іржава" кнопка зі скріншотів
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFDACB),   // персикові плашки
    onPrimaryContainer = Color(0xFF370E00),
    secondary = Color(0xFFF97316),          // яскравий помаранчевий акцент
    onSecondary = Color.White,
    tertiary = Color(0xFF16A34A),           // зелений (Vegan, Saved Offline)
    onTertiary = Color.White,
    background = Color(0xFFFFEDE6),         // ваш фон
    onBackground = Color(0xFF370E00),       // ваш текст
    surface = Color(0xFFFFF8F5),
    onSurface = Color(0xFF370E00),
    surfaceVariant = Color(0xFFF5E4DD),
    onSurfaceVariant = Color(0xFF5A4038),
    outline = Color(0xFFC9A99C),
    error = Color(0xFFB91C1C),
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