package io.horizontalsystems.bankwallet.ui.compose.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme
import kotlin.text.contains


@Composable
fun PasswordInput(
    password: String,
    onPasswordChange: (String) -> Unit = {},
    modifier: Modifier = Modifier.fillMaxWidth(),
    textStyle: TextStyle = TextStyle(
        fontSize = 20.sp,
        color = colorResource(R.color.leah)
    ),
    inLineLabel: Boolean = true
) {
    var isVisible by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val hasFocus = remember { mutableStateOf(false) }
    val nightMode = isSystemInDarkTheme()
    val currentPassword = remember { mutableStateOf(password) }

    val borderColor =  if (nightMode) ComposeAppTheme.colors.gray300 else ComposeAppTheme.colors.dark500

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Spacer(Modifier.height(6.dp))
        if(!inLineLabel){
            Text(
                text = stringResource(R.string.Password),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if(nightMode) ComposeAppTheme.colors.gray200 else ComposeAppTheme.colors.dark600
            )
        }
        BasicTextField(
            value = password,
            onValueChange = { pass -> onPasswordChange(pass) },
            cursorBrush = SolidColor(ComposeAppTheme.colors.primary),
            textStyle = textStyle,
            modifier = modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusChanged { hasFocus.value = it.isFocused }
                .clip(RoundedCornerShape(10.dp))
                .border(
                    width = 2.dp,
                    color = if (hasFocus.value)
                        ComposeAppTheme.colors.primary
                    else
                        borderColor,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(horizontal = 14.dp, vertical = 12.dp),
            visualTransformation = if (isVisible)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),
            decorationBox = { innerTextField ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(
                            id = R.drawable.ic_lock
                        ),
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier
                            .size(24.dp)
                    )
                    Spacer(Modifier.weight(0.02f))
                    if (inLineLabel) {
                        if (!hasFocus.value && currentPassword.value.isEmpty()) {
                            Text(
                                modifier = Modifier.weight(1f),
                                text = stringResource(R.string.Password),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = ComposeAppTheme.colors.dark300
                            )
                        } else {
                            Box(modifier = Modifier.weight(1f)) {
                                innerTextField()
                            }
                        }
                    } else {
                        Box(modifier = Modifier.weight(1f)) {
                            innerTextField()
                        }
                    }

                    Icon(
                        painter = painterResource(id = if (isVisible) R.drawable.eye_show else R.drawable.eye_hide),
                        contentDescription = if (isVisible) "Hide password" else "Show password",
                        tint = Color.Gray,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { isVisible = !isVisible }
                    )
                }
            }
        )
    }
}