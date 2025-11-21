package io.horizontalsystems.bankwallet.modules.authentication.register

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme
import io.horizontalsystems.bankwallet.ui.compose.components.VSpacer

@Composable
fun AvailableCountries() {
    var searchCountry by rememberSaveable { mutableStateOf("") }
    androidx.compose.foundation.layout.Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 13.dp, end = 13.dp)
    ) {
        Text(
            text = "What country do you live in?",
            modifier = Modifier.padding(end = 26.dp),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = ComposeAppTheme.colors.leah
        )
        Spacer(Modifier.height(7.dp))
        BasicTextField(
            value = searchCountry,
            onValueChange = { text -> searchCountry = text },
            cursorBrush = SolidColor(Color.Blue),
            textStyle = TextStyle(
                fontSize = 20.sp,
                color = ComposeAppTheme.colors.leah
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(42.dp)
//                .focusRequester(focusRequester)
//                .onFocusChanged {
//                        it -> boldenEmailOutline.value = it.isFocused
//                }
                .border(
                    2.dp,
                    colorResource(R.color.leah),
                    RoundedCornerShape(8.dp)
                )
                .background(color = ComposeAppTheme.colors.blade)
                .padding(12.dp),
            decorationBox = ({ innerTextField ->
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "Search",
                        tint = Color.Gray,
                        modifier = Modifier
                            .height(40.dp)
                    )
                    Spacer(Modifier.width(5.dp))
                    innerTextField()
                }
            })
        )
    }

}