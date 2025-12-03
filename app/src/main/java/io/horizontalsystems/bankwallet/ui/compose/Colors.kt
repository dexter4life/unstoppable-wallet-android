package io.horizontalsystems.bankwallet.ui.compose

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

data class ThemeColors(
        val primary: Color,
        val primaryPressed: Color,
        val primaryDisabled: Color,
        val secondary: Color,
        val secondaryPressed: Color,
        val secondaryDisabled: Color,
        val background: Color,
        val surface: Color,
        val border: Color,
        val error: Color,
        val errorPressed: Color
)

data class ColorShades(
        val c0: Color,
        val c1: Color,
        val c2: Color,
        val c3: Color,
        val c4: Color,
        val c5: Color,
        val c6: Color,
        val c7: Color,
        val c8: Color,
        val c9: Color
)


/// **
// * Light theme
// */
 val LightTheme = ThemeColors(
    primary = Blue.c6,
    primaryPressed = Blue.c7,
    primaryDisabled = Blue.c2,

    secondary = Teal.c6,
    secondaryPressed = Teal.c7,
    secondaryDisabled =Teal.c2,

    background = White,
    surface = Gray.c0,
    border = Gray.c3,

    error = Red.c6,
    errorPressed = Red.c7
 )

//
/// **
// * Dark theme
// */
// val DarkTheme = ThemeColors(
//    primary = Colors.Blue.c4,
//    primaryPressed = Colors.Blue.c5,
//    primaryDisabled = Colors.Blue.c8,
//
//    secondary = Colors.Teal.c4,
//    secondaryPressed = Colors.Teal.c5,
//    secondaryDisabled = Colors.Teal.c8,
//
//    background = Colors.Gray.c9,
//    surface = Colors.Gray.c8,
//    border = Colors.Gray.c6,
//
//    error = Colors.Red.c4,
//    errorPressed = Colors.Red.c5,
//
//    onPrimary = Color.White,
//    onSecondary = Color.White,
//    onBackground = Color.White,
//    onSurface = Color.White,
//    onError = Color.White
// )

@Stable
class Colors(
    jacob: Color,
    remus: Color,
    lucian: Color,
    tyler: Color,
    leah: Color,
    lawrence: Color,
    laguna: Color,
    raina: Color,
    andy: Color,
    blade: Color,
) {


    //base colors
    val transparent = Color.Transparent
    val dark = Dark
    val light = Light
    val white = Color.White
    val black50 = Black50
    val issykBlue = Color(0xFF3372FF)
    val lightGrey = LightGrey
    val grey = Grey
    val yellow50 = Yellow50
    val yellow20 = Yellow20
    val green20 = Green20

val primary = YellowD

val primaryLight = YellowL

    val greenD = GreenD
    val greenL = GreenL
    val green50 = Green50
    val redD = RedD
    val redL = RedL
    val elenaD = Color(0xFF6E7899)
    val red50 = Red50
    val red20 = Red20

// Gray scale colors
val gray50 = Color(0xFFF8F9FA)
val gray100 = Color(0xFFF1F3F5)
val gray200 = Color(0xFFE9ECEF)
val gray300 = Color(0xFFDEE2E6)
val gray400 = Color(0xFFCED4DA)
val gray500 = Color(0xFFADB5BD)
val gray600 = Color(0xFF868E96)
val gray700 = Color(0xFF495057)
val gray800 = Color(0xFF343A40)
val gray900 = Color(0xFF212529)

// Dark theme colors
val dark50 = Color(0xFFC9C9C9)
val dark100 = Color(0xFFB8B8B8)
val dark200 = Color(0xFF828282)
val dark300 = Color(0xFF696969)
val dark400 = Color(0xFF424242)
val dark500 = Color(0xFF3B3B3B)
val dark600 = Color(0xFF2E2E2E)
val dark700 = Color(0xFF242424)
val dark800 = Color(0xFF1F1F1F)
val dark900 = Color(0xFF141414)

    //themed colors
    var jacob by mutableStateOf(jacob)
        private set
    var remus by mutableStateOf(remus)
        private set
    var lucian by mutableStateOf(lucian)
        private set
    var tyler by mutableStateOf(tyler)
        private set
    var leah by mutableStateOf(leah)
        private set
    var lawrence by mutableStateOf(lawrence)
        private set
    var laguna by mutableStateOf(laguna)
        private set
    var raina by mutableStateOf(raina)
        private set
    var andy by mutableStateOf(andy)
        private set
    var blade by mutableStateOf(blade)
        private set

    fun update(other: Colors) {
        jacob = other.jacob
        remus = other.remus
        lucian = other.lucian
        tyler = other.tyler
        leah = other.leah
        lawrence = other.lawrence
        laguna = other.laguna
        raina = other.raina
        andy = other.andy
        blade = other.blade
    }

    fun copy(): Colors = Colors(
        jacob = jacob,
        remus = remus,
        lucian = lucian,
        tyler = tyler,
        leah = leah,
        lawrence = lawrence,
        laguna = laguna,
        raina = raina,
        andy = andy,
        blade = blade,
    )
}
