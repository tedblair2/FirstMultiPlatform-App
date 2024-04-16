package theme

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val lightColorScheme=AppColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground
)

private val darkColorScheme=AppColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground
)

private val typography=AppTypography(
    titleLarge = TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
    ),
    titleNormal = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold
    ),
    body = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.Normal
    ),
    labelLarge = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold
    ),
    labelNormal = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal
    ),
    labelSmall = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal
    )
)

private val shape=AppShape(
    container = RoundedCornerShape(12.dp),
    button = RoundedCornerShape(10.dp)
)

private val size=AppSize(
    large = 24.dp,
    medium = 20.dp,
    normal = 18.dp,
    small = 14.dp
)

@Composable
fun AppTheme(
    isDarkTheme:Boolean = isSystemInDarkTheme(),
    content:@Composable ()->Unit
) {
    val colorScheme= if (isDarkTheme) darkColorScheme else lightColorScheme
    val rippleIndication= rememberRipple()

    CompositionLocalProvider(
        LocalAppColorScheme provides colorScheme,
        LocalAppTypography provides typography,
        LocalAppShape provides shape,
        LocalAppSize provides size,
        LocalIndication provides rippleIndication,
        content = content
    )
}

object AppTheme {
    val colorScheme:AppColorScheme
        @Composable get() = LocalAppColorScheme.current

    val typography:AppTypography
        @Composable get() = LocalAppTypography.current

    val shape:AppShape
        @Composable get() = LocalAppShape.current

    val size:AppSize
        @Composable get() = LocalAppSize.current
}