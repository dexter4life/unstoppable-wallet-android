package io.horizontalsystems.bankwallet.modules.authentication.register

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme
import io.horizontalsystems.bankwallet.ui.compose.components.OTP_VIEW_TYPE_BORDER
import io.horizontalsystems.bankwallet.ui.compose.components.OtpView


@Composable
fun VerifyEmailNotification(
    email: String = "dexter4life@gmail.com",
    onConfirmSuccess: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val nightMode = isSystemInDarkTheme()
    val borderColor =
        if (nightMode) ComposeAppTheme.colors.gray50 else ComposeAppTheme.colors.dark800
    val otpValue  =  remember { mutableStateOf("") }

    val otpBorderColor = if (nightMode) ComposeAppTheme.colors.gray50 else ComposeAppTheme.colors.dark800

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = if (nightMode) ComposeAppTheme.colors.dark else ComposeAppTheme.colors.white
            )
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
       Column(
           horizontalAlignment = Alignment.CenterHorizontally,
           verticalArrangement = Arrangement.Center,
           modifier = Modifier.weight(1f)
       ) {
           Text(
               text = "Verify your email",
               fontSize = 28.sp,
               fontWeight = FontWeight.Normal,
               textAlign = TextAlign.Center,
               color = if (nightMode) ComposeAppTheme.colors.gray100 else Color.Black
           )
           Spacer(Modifier.height(12.dp))
           Text(
               text = "We've emailed a one time security code to $email" +
                       ", please enter it below",
               fontSize = 18.sp,
               textAlign = TextAlign.Center,
               color = if (nightMode) ComposeAppTheme.colors.dark50 else ComposeAppTheme.colors.dark300,
               modifier = Modifier.padding(bottom = 48.dp)
           )

           OtpView(
               otpText = otpValue.value,
               onOtpTextChange = {
                   value -> otpValue.value = value
               },
               type = OTP_VIEW_TYPE_BORDER,
               password = false,
               containerSize = 38.dp,
               otpCount = 7,
               charColor = otpBorderColor,
               borderColor = otpBorderColor,
               splitAt = 3,
               charSize = 18.sp
           )

           Spacer(Modifier.height(30.dp))
           Button(
               onClick = {
                   onConfirmSuccess()
               },
               modifier = Modifier
                   .fillMaxWidth()
                   .border(
                       1.dp, Color.Transparent,
                       RoundedCornerShape(28.dp)
                   )
                   .height(56.dp),
               shape = RoundedCornerShape(28.dp),
               colors = ButtonDefaults.buttonColors(
                   containerColor = ComposeAppTheme.colors.primary
               )
           ) {
               Row(
                   verticalAlignment = Alignment.CenterVertically,
                   horizontalArrangement = Arrangement.Center,
               ) {
                   Text(
                       text = stringResource(R.string.Button_confirm_email),
                       color = if (nightMode) ComposeAppTheme.colors.gray50
                       else ComposeAppTheme.colors.dark800,
                       fontSize = 18.sp,
                       fontWeight = FontWeight.Medium
                   )
               }
           }
           Spacer(Modifier.height(12.dp))
           Button(
               onClick = onBackClick,
               modifier = Modifier
                   .fillMaxWidth()
                   .border(
                       1.dp, borderColor,
                       RoundedCornerShape(28.dp)
                   )
                   .height(56.dp),
               shape = RoundedCornerShape(28.dp),
               colors = ButtonDefaults.buttonColors(
                   containerColor = Color.Transparent
               )
           ) {
               Text(
                   text = "Go Back",
                   color = if (nightMode) ComposeAppTheme.colors.gray50 else ComposeAppTheme.colors.dark800,
                   fontSize = 18.sp,
                   fontWeight = FontWeight.Medium
               )
           }
       }

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
            fontSize = 16.sp,
            textAlign = TextAlign.Left,
            color = if (nightMode) ComposeAppTheme.colors.gray500
            else ComposeAppTheme.colors.dark400
        )
    }

}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun VerifyEmailNotificationPreviewLight() {
    ComposeAppTheme(darkTheme = false) {
        VerifyEmailNotification(
            email = "dexter4life@gmail.com"
        )
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun VerifyEmailNotificationPreviewDark() {
    ComposeAppTheme(darkTheme = true) {
        VerifyEmailNotification(
            email = "dexter4life@gmail.com"
        )
    }
}