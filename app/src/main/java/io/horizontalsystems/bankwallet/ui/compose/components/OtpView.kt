package io.horizontalsystems.bankwallet.ui.compose.components

import android.content.res.Configuration
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme

const val OTP_VIEW_TYPE_UNDERLINE = 1
const val OTP_VIEW_TYPE_BORDER = 2


@Composable
fun OtpView(
    otpText: String,
    modifier: Modifier = Modifier,
    charColor: Color = Color.Black,
    borderColor: Color = Color.Black,
    charBackground: Color = Color.Transparent,
    charSize: TextUnit = 16.sp,
    containerSize: Dp = charSize.value.dp * 2,
    containerRadius: Dp = 12.dp,
    otpCount: Int = 4,
    type: Int = OTP_VIEW_TYPE_UNDERLINE,
    enabled: Boolean = true,
    password: Boolean = false,
    passwordChar: String = "",
    onOtpTextChange: (String) -> Unit,
    splitAt: Int = -1
) {
    val isFocusedState = remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    BasicTextField(
        modifier = modifier
            .focusRequester(focusRequester)
            .onFocusChanged { isFocusedState.value = it.isFocused },
        value = otpText,
        onValueChange = {
            if (it.length <= otpCount - 1) {
                onOtpTextChange.invoke(it)
            }
        },
        enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        decorationBox = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (splitAt > 0) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.border(
                            width = 1.dp,
                            color = borderColor,
                            shape = RoundedCornerShape(
                                containerRadius
                            )
                        )
                    ) {
                        repeat(otpCount / splitAt + 1) { index ->
                            CharView(
                                index = index,
                                otpCount = otpCount,
                                text = otpText,
                                charColor = charColor,
                                highlightColor = borderColor,
                                charSize = charSize,
                                containerRadius = containerRadius,
                                containerSize = containerSize,
                                type = type,
                                charBackground = charBackground,
                                password = password,
                                passwordChar = passwordChar,
                            )
                        }
                    }
                    Divider(
                        modifier = Modifier
                            .padding(start = 8.dp, end = 8.dp)
                            .width(15.dp)
                            .height(1.5.dp),
                        color = ComposeAppTheme.colors.dark100
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.border(
                            width = 1.dp,
                            color = borderColor,
                            shape = RoundedCornerShape(
                                containerRadius
                            )
                        )
                    ) {
                        repeat(otpCount - (otpCount / splitAt) - 2) { index ->
                            CharView(
                                index = index + splitAt,
                                otpCount = otpCount,
                                text = otpText,
                                charColor = charColor,
                                highlightColor = borderColor,
                                charSize = charSize,
                                containerRadius = containerRadius,
                                containerSize = containerSize,
                                type = type,
                                charBackground = charBackground,
                                password = password,
                                passwordChar = passwordChar,
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun CharView(
    index: Int,
    otpCount: Int,
    text: String,
    charColor: Color,
    highlightColor: Color,
    charSize: TextUnit,
    containerSize: Dp,
    containerRadius: Dp,
    type: Int = OTP_VIEW_TYPE_UNDERLINE,
    charBackground: Color = Color.Transparent,
    password: Boolean = false,
    passwordChar: String = "",
) {
    var modifier = if (type == OTP_VIEW_TYPE_BORDER) {
        Modifier.size(containerSize)
    } else Modifier
        .width(containerSize)
        .background(charBackground)

    if (type == OTP_VIEW_TYPE_BORDER) {
        if (index == 0 || index == 1 || index == 3 || index == 4) {
            modifier = modifier
                .drawBehind {
                    val strokeWidthPx = 1.dp.toPx()
                    val height = size.height
                    drawLine(
                        color = highlightColor,
                        start = Offset(x = size.width, y = 1f),
                        end = Offset(x = size.width, y = height - 1),
                        strokeWidth = strokeWidthPx,
                        cap = StrokeCap.Square
                    )
                }
        }

        modifier = modifier
            .height(containerSize)
            .clip(RoundedCornerShape(containerRadius))
            .background(charBackground)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val char = when {
            index >= text.length -> ""
            password -> passwordChar
            else -> text[index].toString()
        }

        // Determine if caret should be shown for this index
        val showCaret = (index == text.length && text.length < otpCount) ||
                (index == otpCount - 1 && text.length == otpCount)

        // Blinking animation for caret
        val transition = rememberInfiniteTransition(label = "otpCaret")
        val caretAlpha = transition.animateFloat(
            initialValue = 1f,
            targetValue = 0f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 700),
                repeatMode = RepeatMode.Reverse
            ),
            label = "otpCaretAlpha"
        ).value

        // Content box to stack text and caret
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Text(
                text = char,
                color = charColor,
                style = ComposeAppTheme.typography.bodyR,
                fontSize = charSize,
                textAlign = TextAlign.Center
            )

            if (showCaret) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .offset(y = 2.dp)
                        .height(containerSize * 0.6f)
                        .background(ComposeAppTheme.colors.primary.copy(alpha = caretAlpha))
                )
            }
        }
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun VerifyEmailNotificationPreviewLight() {
    val text = remember { mutableStateOf("") }
    ComposeAppTheme(darkTheme = false) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 20.dp)
        ) {
            OtpView(
                otpText = text.value,
                onOtpTextChange = { value ->
                    text.value = value
                },
                type = OTP_VIEW_TYPE_BORDER,
                password = false,
                containerSize = 42.dp,
                otpCount = 7,
                charColor = Color.Black,
                splitAt = 3,
                charSize = 30.sp
            )
        }
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun VerifyEmailNotificationPreviewDark() {
    val text = remember { mutableStateOf("") }
    ComposeAppTheme(darkTheme = true) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 30.dp)
        ) {
            OtpView(
                otpText = text.value,
                onOtpTextChange = { value ->
                    text.value = value
                },
                borderColor = Color.White,
                type = OTP_VIEW_TYPE_BORDER,
                password = false,
                containerSize = 48.dp,
                otpCount = 7,
                charColor = Color.White,
                splitAt = 3,
                charSize = 30.sp
            )
        }
    }
}