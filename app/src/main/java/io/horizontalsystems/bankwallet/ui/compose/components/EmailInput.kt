package io.horizontalsystems.bankwallet.ui.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme


@Composable
fun EmailInput(
    email: String,
    onEmailChange: (String) -> Unit = {},
    modifier: Modifier = Modifier.fillMaxWidth(),
    nightMode: Boolean,
    textStyle: TextStyle = TextStyle(
        fontSize = 20.sp,
        color = colorResource(R.color.leah)
    ),
    inLineLabel: Boolean = true
) {
    val focusRequester = remember { FocusRequester() }
    val hasFocus = remember { mutableStateOf(false) }
    val currentValue = remember { mutableStateOf(email) }
    val borderColor =  if (nightMode) ComposeAppTheme.colors.gray300 else ComposeAppTheme.colors.dark500

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        if (!inLineLabel) {
            Text(
                text = stringResource(R.string.Input_Email),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if(nightMode) ComposeAppTheme.colors.gray200 else ComposeAppTheme.colors.dark600
            )
        }
        BasicTextField(
            value = email,
            onValueChange = {
                onEmailChange(it)
                currentValue.value = it
            },
            cursorBrush = SolidColor(ComposeAppTheme.colors.primary),
            textStyle = textStyle,
            modifier = modifier
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
                    if (inLineLabel) {
                        if (!hasFocus.value && currentValue.value.isEmpty())
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
                    } else {
                        Box(modifier = Modifier.weight(1f)) {
                            innerTextField()
                        }
                    }
                }
            }
        )
    }
}


@Composable
fun EmailInputOutline(
    email: String,
    onEmailChange: (String) -> Unit = {},
    modifier: Modifier = Modifier.fillMaxWidth(),
    nightMode: Boolean,
    textStyle: TextStyle = TextStyle(
        fontSize = 20.sp,
        color = colorResource(R.color.leah)
    )
) {
    val borderColor = remember { mutableStateOf(Color.Transparent) }


    Text(
        text = "Your email",
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold,
        style = ComposeAppTheme.typography.subheadSB,
        color =
            if (nightMode) ComposeAppTheme.colors.gray200
            else ComposeAppTheme.colors.dark600,
        modifier = Modifier.padding(bottom = 12.dp)
    )
    OutlinedTextField(
        value = email,
        onValueChange = onEmailChange,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .border(
                width = 2.dp,
                color = ComposeAppTheme.colors.dark200,
                shape = RoundedCornerShape(10.dp)
            ),
        singleLine = true,
        textStyle =
            ComposeAppTheme.typography.bodyR.copy(
                fontSize = 18.sp,
                color =
                    if (nightMode) ComposeAppTheme.colors.gray100
                    else ComposeAppTheme.colors.dark,
                fontWeight = FontWeight.Medium
            ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = ComposeAppTheme.colors.gray400,
//                unfocusedBorderColor = ComposeAppTheme.colors.dark500,
            cursorColor = ComposeAppTheme.colors.leah
        )
    )

}