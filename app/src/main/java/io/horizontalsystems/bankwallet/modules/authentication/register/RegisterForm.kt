package io.horizontalsystems.bankwallet.modules.authentication.register

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.ui.compose.components.EmailInput
import io.horizontalsystems.bankwallet.ui.compose.components.PasswordInput
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme
import io.horizontalsystems.bankwallet.ui.compose.components.ButtonPrimary
import io.horizontalsystems.bankwallet.ui.compose.components.ButtonPrimaryDefaults
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle


@Composable
fun StrengthBar(strength: Int, nightMode: Boolean) {
    val totalSegments = 4

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
            .clip(RoundedCornerShape(2.dp)),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        repeat(totalSegments) { index ->
            val isActive = index < strength

            val color = animateColorAsState(
                targetValue = when {
                    !isActive -> if (nightMode) ComposeAppTheme.colors.dark400
                    else ComposeAppTheme.colors.gray200           // grey (inactive)
                    strength <= 1 -> Color(0xFFE53935)     // red
                    strength == 2 -> Color(0xFFFFC107)     // yellow
                    true -> Color(0xFF43A047)     // green
                    else -> Color.LightGray
                }, label = ""
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(color.value, RoundedCornerShape(5.dp))
            )
        }
    }
}

@Composable
fun RequirementText(text: String, nightMode: Boolean) {
    Text(
        text, fontSize = 14.sp, color = if (nightMode) ComposeAppTheme.colors.gray50
        else ComposeAppTheme.colors.dark500, modifier = Modifier.padding(bottom = 4.dp)
    )
}

@Composable
fun PasswordRequirements(password: String, nightMode: Boolean) {

    val requirements = listOf(
        Regex(".*[@!#\$%^&*\\-==+].*") to "1 special character (@!#\$%^&*-==+)",
        Regex(".*[A-Za-z].*") to "1 letter",
        Regex(".*\\d.*") to "1 number",
        Regex(".{8,}") to "8 characters"
    )

    val passedCount = remember(password) {
        requirements.count { password.matches(it.first) }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 5.dp, end = 5.dp)
    ) {
        StrengthBar(strength = passedCount, nightMode = nightMode)
        Spacer(Modifier.height(12.dp))
        Text(
            "Password must contain the following:",
            fontSize = 15.sp,
            color = if (nightMode) ComposeAppTheme.colors.dark50
            else ComposeAppTheme.colors.dark500
        )
        Spacer(Modifier.height(8.dp))

        requirements.forEach { (_, text) ->
            RequirementText(text, nightMode = nightMode)
        }
    }
}


@Composable
fun RegisterForm(nightMode: Boolean = isSystemInDarkTheme(), onContinue: () -> Unit = {}) {
    var password by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .background(
                color = if (nightMode) ComposeAppTheme.colors.dark800
                else ComposeAppTheme.colors.gray50
            )
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    lineHeight = 30.sp,
                    text = "Enter your email and password",
                    textAlign = TextAlign.Left,
                    color = colorResource(R.color.leah)
                )
                Spacer(Modifier.height(20.dp))
                EmailInput(
                    email = email,
                    onEmailChange = { em -> email = em },
                    nightMode = nightMode,
                    modifier = Modifier
                        .height(60.dp)
                        .fillMaxWidth(),
                    textStyle = TextStyle(
                        fontSize = 18.sp,
                        color = colorResource(R.color.leah)
                    ),
                    inLineLabel = false
                )
                Spacer(Modifier.height(15.dp))
                PasswordInput(
                    password = password, onPasswordChange = { pass -> password = pass },
                    modifier = Modifier
                        .height(60.dp)
                        .fillMaxWidth(),
                    textStyle = TextStyle(
                        fontSize = 18.sp,
                        color = colorResource(R.color.leah)
                    ),
                    inLineLabel = false
                )
                Spacer(Modifier.height(20.dp))
                PasswordRequirements(password = password, nightMode = nightMode)
                Spacer(Modifier.height(20.dp))

            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = buildAnnotatedString() {
                        append("By creating an account, you consent that you have read and agree to our ")
                        withStyle(
                            style = SpanStyle(
                                textDecoration = TextDecoration.Underline,
                                fontWeight = FontWeight.Medium,
                                color = ComposeAppTheme.colors.issykBlue
                            )
                        ) {
                            append("Terms of Services")
                        }

                        append(" and ")

                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Medium,
                                color = ComposeAppTheme.colors.issykBlue,
                            )
                        ) {
                            append("Privacy Policy.")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Left,
                    color = if (nightMode) ComposeAppTheme.colors.gray500
                    else ComposeAppTheme.colors.dark400
                )
                Spacer(Modifier.height(20.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                ) {
                    ButtonPrimary(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        enabled = email.isNotBlank() && password.isNotBlank(),
                        onClick = onContinue,
                        buttonColors =
                            ButtonPrimaryDefaults.textButtonColors(
                                backgroundColor = ComposeAppTheme.colors.primary,
                                contentColor = ComposeAppTheme.colors.dark,
                                disabledBackgroundColor = if (nightMode) ComposeAppTheme.colors.dark700
                                else ComposeAppTheme.colors.gray200,
                                disabledContentColor = if (nightMode) ComposeAppTheme.colors.dark300
                                else ComposeAppTheme.colors.dark100,
                            )
                    ) {
                        Text(
                            text = "Continue",
                            style = ComposeAppTheme.typography.headline2,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }

}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun Preview_RegisterLight() {
    val nightMode = isSystemInDarkTheme()
    ComposeAppTheme(darkTheme = false) {
        RegisterView(
            {}, nightMode = nightMode
        ) {
            RegisterForm(nightMode = nightMode)
        }

    }
}


@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Preview_RegisterDark() {
    val nightMode = isSystemInDarkTheme()
    ComposeAppTheme(darkTheme = true) {
        RegisterView(
            {}, nightMode = nightMode
        ) {
            RegisterForm(nightMode = nightMode)
        }

    }
}