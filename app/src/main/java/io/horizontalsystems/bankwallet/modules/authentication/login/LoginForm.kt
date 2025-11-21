package io.horizontalsystems.bankwallet.modules.authentication.login

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme
import io.horizontalsystems.bankwallet.ui.compose.components.ButtonPrimary
import io.horizontalsystems.bankwallet.ui.compose.components.ButtonPrimaryDefaults


@Composable
fun LoginForm(viewModel:  LoginActivityModel) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val boldenEmailOutline = remember { mutableStateOf(false) }
    val boldenPasswordOutline = remember { mutableStateOf(false) }
    var isVisible by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.Input_Email),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.leah)
            )
            Spacer(Modifier.height(6.dp))
            BasicTextField(
                value = email,
                onValueChange = { pass -> email = pass},
                cursorBrush = SolidColor(Color.Blue),
                textStyle = TextStyle(
                    fontSize = 20.sp,
                    color = colorResource(R.color.leah)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .focusRequester(focusRequester)
                    .onFocusChanged {
                            it -> boldenEmailOutline.value = it.isFocused
                    }
                    .border(if (boldenEmailOutline.value) 3.dp else 2.dp,
                        if (boldenEmailOutline.value) ComposeAppTheme.colors.yellowD else colorResource(R.color.leah),
                        RoundedCornerShape(8.dp))
                    .padding(12.dp)
            )
        }
        Spacer(Modifier.height(20.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.Password),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.leah)
            )
            Spacer(Modifier.height(6.dp))
            BasicTextField(
                value = password,
                onValueChange = { pass ->  password = pass },
                textStyle = TextStyle(fontSize = 18.sp, color = ComposeAppTheme.colors.leah),
                modifier = Modifier.focusRequester(focusRequester)
                    .onFocusChanged {
                            it -> boldenPasswordOutline.value = it.isFocused
                    },
                visualTransformation = if (isVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .border(if (boldenPasswordOutline.value) 3.dp else 2.dp,
                                if (boldenPasswordOutline.value) ComposeAppTheme.colors.yellowD else ComposeAppTheme.colors.leah,
                                RoundedCornerShape(8.dp))
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            innerTextField()
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
        Spacer(Modifier.height(25.dp))
        ButtonPrimary(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {},
            buttonColors = ButtonPrimaryDefaults.textButtonColors(
                backgroundColor = ComposeAppTheme.colors.yellowD,
                contentColor = ComposeAppTheme.colors.dark,
                disabledBackgroundColor = ComposeAppTheme.colors.blade,
                disabledContentColor = ComposeAppTheme.colors.andy,
            ),
            content = {
//            CircularProgressIndicator(
//                modifier = Modifier.size(16.dp),
//                color = ComposeAppTheme.colors.grey,
//                strokeWidth = 2.dp
//            )
//            HSpacer(width = 8.dp)

                Text(
                    text = stringResource(R.string.Button_Login),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            enabled = true
        )
    }
}