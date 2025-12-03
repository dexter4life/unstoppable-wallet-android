package io.horizontalsystems.bankwallet.modules.authentication.login

import android.content.res.Configuration
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.modules.authentication.AuthActivityModel
import io.horizontalsystems.bankwallet.ui.compose.components.EmailInput
import io.horizontalsystems.bankwallet.ui.compose.components.PasswordInput
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme
import io.horizontalsystems.bankwallet.ui.compose.components.ButtonPrimary
import io.horizontalsystems.bankwallet.ui.compose.components.ButtonPrimaryDefaults


@Composable
fun EmailInput(viewModel: AuthActivityModel, isNight: Boolean) {
    var email by rememberSaveable { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val hasFocus = remember { mutableStateOf(false) }
    BasicTextField(
        value = email,
        onValueChange = { email = it },
        cursorBrush = SolidColor(Color.Blue),
        textStyle = TextStyle(
            fontSize = 20.sp,
            color = colorResource(R.color.leah)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .focusRequester(focusRequester)
            .onFocusChanged { hasFocus.value = it.isFocused }
            .clip(RoundedCornerShape(16.dp))
            .background(if (isNight) ComposeAppTheme.colors.dark600 else ComposeAppTheme.colors.gray100)
            .border(
                width = 2.dp,
                color = if (hasFocus.value)
                    ComposeAppTheme.colors.primary
                else
                    if (isNight) ComposeAppTheme.colors.dark600 else ComposeAppTheme.colors.gray100,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 14.dp, vertical = 12.dp),
        decorationBox = { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(
                        id = R.drawable.ic_email
                    ),
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(24.dp)
                )
                Spacer(Modifier.width(10.dp))
                if (!hasFocus.value)
                    Text(
                        text = stringResource(R.string.Input_Email),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = ComposeAppTheme.colors.dark300
                    )
                else
                    Box(modifier = Modifier.weight(1f)) {
                        innerTextField()
                    }
            }
        }
    )
}

@Composable
fun PasswordInput(viewModel: AuthActivityModel, isNight: Boolean) {
    var password by rememberSaveable { mutableStateOf("") }
    var isVisible by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val hasFocus = remember { mutableStateOf(false) }
    val isNight = isSystemInDarkTheme()


    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(Modifier.height(6.dp))
        BasicTextField(
            value = password,
            onValueChange = { pass -> password = pass },
            textStyle = TextStyle(fontSize = 18.sp, color = ComposeAppTheme.colors.leah),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .focusRequester(focusRequester)
                .onFocusChanged { hasFocus.value = it.isFocused }
                .clip(RoundedCornerShape(16.dp))
                .background(if (isNight) ComposeAppTheme.colors.dark600 else ComposeAppTheme.colors.gray100)
                .border(
                    width = 2.dp,
                    color = if (hasFocus.value)
                        ComposeAppTheme.colors.primary
                    else
                        if (isNight) ComposeAppTheme.colors.dark600 else ComposeAppTheme.colors.gray100,
                    shape = RoundedCornerShape(16.dp)
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
                    if (!hasFocus.value)
                        Text(
                            modifier = Modifier.weight(1f),
                            text = stringResource(R.string.Password),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = ComposeAppTheme.colors.dark300
                        )
                    else
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
}

@Composable
fun LoginForm(viewModel: AuthActivityModel, navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        var password by rememberSaveable { mutableStateOf("") }
        var email by rememberSaveable { mutableStateOf("") }
        val nightMode = isSystemInDarkTheme()

        EmailInput(
            email = email,
            onEmailChange = { em -> email = em },
            nightMode = nightMode,
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(),
        )
        Spacer(Modifier.height(15.dp))
        PasswordInput(
            password = password, onPasswordChange = { pass -> password = pass },
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(),
        )
        Spacer(Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(
                text = stringResource(R.string.Button_ForgotPassword),
                fontWeight = FontWeight.Medium,
                color = ComposeAppTheme.colors.primary,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable(enabled = true, onClick = {
                    navController.navigate("forgot_password")
                }),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(Modifier.height(16.dp))
        ButtonPrimary(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {},
            buttonColors = ButtonPrimaryDefaults.textButtonColors(
                backgroundColor = ComposeAppTheme.colors.primary,
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
                    fontSize = 18.sp,
                    color = ComposeAppTheme.colors.dark
                )
            }
        )
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun Preview_Login() {
    val navController = rememberNavController()
    ComposeAppTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 105.dp, start = 13.dp, end = 13.dp)
                .background(color = ComposeAppTheme.colors.white)
                .clip(RoundedCornerShape(16.dp)),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LoginForm(AuthActivityModel(null), navController)
        }
    }
}

