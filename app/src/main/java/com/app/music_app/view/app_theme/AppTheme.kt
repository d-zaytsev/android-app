package com.app.music_app.view.app_theme

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android_app.R
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/**
 * Впихивает значения в контекст
 */
@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {

    val rippleIndication = rememberRipple()
    // Из accompanist, доп. функции
    val systemUiController = rememberSystemUiController()

    // Проталкиваем наши значения в контекст под определёнными ключами
    CompositionLocalProvider(
        LocalAppColorScheme provides definedColor,
        LocalAppSize provides definedSize,
        LocalAppTypography provides definedTypography,
        LocalAppShape provides definedShape,
        LocalIndication provides rippleIndication,
        content = content
    )

    // Статус бар кастомный
    systemUiController.setSystemBarsColor(
        color = AppColor.WhiteSmoke
    )
}

/**
 * Позволяет обращаться к значениям (выцепляет их из контекса по ключу)
 */
object AppTheme {
    val color: AppColorScheme
        @Composable get() = LocalAppColorScheme.current
    val size: AppSize
        @Composable get() = LocalAppSize.current

    val typography: AppTypography
        @Composable get() = LocalAppTypography.current

    val shape: AppShape
        @Composable get() = LocalAppShape.current
}

object AppColor {
    // https://coolors.co/palette/03045e-0077b6-00b4d8-90e0ef-caf0f8 - App Palette
    val CoralPink: Color
        get() = Color(248, 131, 121)
    val Emerald: Color
        get() = Color(80, 200, 120)
    val LightCyan: Color
        get() = Color(202, 240, 248)
    val NonPhotoBlue: Color
        get() = Color(144, 224, 239)
    val PacificCyan: Color
        get() = Color(0, 180, 216)
    val HonoluluBlue: Color
        get() = Color(0, 119, 182)
    val FederalBlue: Color
        get() = Color(3, 4, 94)
    val WhiteSmoke: Color
        get() = Color(245, 245, 245)
    val TimberWolf: Color
        get() = Color(219, 215, 210)
    val IndiaGreen: Color
        get() = Color(19, 136, 8)
}

object AppFontFamily {

    val Roboto: FontFamily
        get() = FontFamily(
            Font(R.font.roboto_regular),
            Font(R.font.roboto_black, FontWeight.Black),
            Font(R.font.roboto_bold, FontWeight.Bold),
            Font(R.font.roboto_light, FontWeight.Light),
            Font(R.font.roboto_medium, FontWeight.Medium),
            Font(R.font.roboto_thin, FontWeight.Thin),
            Font(R.font.roboto_black_italic, FontWeight.Black, style = FontStyle.Italic),
            Font(R.font.roboto_light_italic, FontWeight.Light, style = FontStyle.Italic),
            Font(R.font.roboto_medium_italic, FontWeight.Medium, style = FontStyle.Italic),
            Font(R.font.roboto_bold_italic, FontWeight.Bold, style = FontStyle.Italic),
            Font(R.font.roboto_thin_italic, FontWeight.Thin, style = FontStyle.Italic),
            Font(R.font.roboto_italic, style = FontStyle.Italic),

            )
}

private val definedColor = AppColorScheme(
    surface = AppColor.WhiteSmoke,
    onSurface = Color.Black,
    primary = AppColor.HonoluluBlue,
    onPrimary = AppColor.WhiteSmoke,
    secondary = AppColor.PacificCyan,
    onSecondary = AppColor.LightCyan,
    tertiary = AppColor.NonPhotoBlue,
    onTertiary = AppColor.PacificCyan,
    error = AppColor.CoralPink,
    onError = AppColor.PacificCyan,
    success = AppColor.Emerald,
    onSuccess = AppColor.IndiaGreen,
    outline = AppColor.TimberWolf,
    outlineVariant = AppColor.TimberWolf,
    inactive = Color.Gray
)

private val definedSize = AppSize(
    large = 30.dp,
    medium = 24.dp,
    normal = 16.dp,
    small = 12.dp
)

private val definedTypography = AppTypography(
    display = TextStyle(
        fontFamily = AppFontFamily.Roboto,
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp
    ),
    title = TextStyle(
        fontFamily = AppFontFamily.Roboto,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp
    ),
    body = TextStyle(
        fontFamily = AppFontFamily.Roboto,
        fontSize = 24.sp
    ),
    label = TextStyle(
        fontFamily = AppFontFamily.Roboto,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp
    )
)

private val definedShape = AppShape(
    button = RoundedCornerShape(10.dp),
    container = RoundedCornerShape(15.dp)
)