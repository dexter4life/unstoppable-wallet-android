package io.horizontalsystems.bankwallet.ui.compose.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.core.providers.resource.CountryCode
import io.horizontalsystems.bankwallet.core.providers.resource.CountryFlag
import io.horizontalsystems.bankwallet.modules.authentication.register.RegisterActivityModel
import io.horizontalsystems.bankwallet.modules.coin.overview.ui.Loading
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme


@Composable
private fun CountryCodeItem(
    item: CountryCode,
    flag: CountryFlag?,
    onCountryClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCountryClick.invoke(item.code) }
            .padding(horizontal = 16.dp)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically,

        ) {
        when {
            flag != null -> Image(
                painter = painterResource(flag.flagId),
                contentDescription = item.name,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(28.dp)
                    .clip(CircleShape),
            )

            else -> Image(
                painter = painterResource(R.drawable.ic_globe),
                contentDescription = item.name,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(28.dp)
                    .clip(CircleShape),
            )
        }

        Text(
            text = item.name,
            fontSize = 18.sp,
            color = ComposeAppTheme.colors.leah,
            fontWeight = FontWeight.Medium
        )
        Spacer(Modifier.width(3.dp))
        Text(
            text = item.dialCode,
            fontSize = 18.sp,
            color = ComposeAppTheme.colors.dark200
        )
    }
}

@Composable
private fun NoCountry() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        VSpacer(height = 100.dp)
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(
                    color = ComposeAppTheme.colors.raina,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(48.dp),
                painter = painterResource(R.drawable.ic_globe_20),
                contentDescription = null,
                tint = ComposeAppTheme.colors.grey
            )
        }
        VSpacer(12.dp)
        subhead2_grey(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(R.string.Register_EmptyCountryList),
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
        )
        VSpacer(height = 32.dp)
    }
}

@Composable
private fun CountryCodeList(
    listState: LazyListState = rememberLazyListState(),
    currentItem: CountryCode? = null,
    countryFlags: List<CountryFlag>,
    items: List<CountryCode>,
    onCountryClick: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        LazyColumn(state = listState, userScrollEnabled = true) {
            itemsIndexed(items) { _, item ->
                val flag =
                    countryFlags.find { flag -> flag.code.equals(item.code, true) }
                CountryCodeItem(item, flag, onCountryClick)
                Divider(
                    thickness = 0.5.dp,
                    color = ComposeAppTheme.colors.grey,
                    modifier = Modifier.padding(horizontal = 5.dp)
                )
            }
        }
    }
}


@Composable
fun IntelCodeList(
    countryCodes: List<CountryCode>?,
    currentItem: CountryCode? = null,
    onCountrySelected: (countryCode: String) -> Unit,
    nightMode: Boolean,
    value: String = ""
) {
    val focusRequester = remember { FocusRequester() }
    val backgroundColor =
        if (nightMode) ComposeAppTheme.colors.dark800 else ComposeAppTheme.colors.gray50
    val currentCountry = remember { mutableStateOf(value) }
    val filteredCountry = remember { mutableStateOf(countryCodes) }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = backgroundColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = if (nightMode) ComposeAppTheme.colors.dark900
                    else ComposeAppTheme.colors.gray50
                )
        ) {
            Column(modifier = Modifier.padding(all = 10.dp)) {
                Spacer(Modifier.height(7.dp))
                BasicTextField(
                    value = currentCountry.value,
                    onValueChange = { text ->
                        {

                        }
                    },
                    cursorBrush = SolidColor(Color.Blue),
                    textStyle = TextStyle(
                        fontSize = 16.sp, color = ComposeAppTheme.colors.leah
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .focusRequester(focusRequester)
                        .border(
                            width = 1.dp,
                            color = if (nightMode) ComposeAppTheme.colors.dark200
                            else ComposeAppTheme.colors.gray200,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clip(RoundedCornerShape(8.dp))
                        .background(color = if (nightMode) ComposeAppTheme.colors.dark700 else ComposeAppTheme.colors.gray200)
                        .padding(12.dp),
                    decorationBox = ({ innerTextField ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_search),
                                contentDescription = null,
                                tint = Color.Gray
                            )
                            Spacer(Modifier.width(2.dp))
                            innerTextField()
                        }
                    })
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp) // shadow height
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            if (nightMode) ComposeAppTheme.colors.dark500 else Color.Black.copy(
                                alpha = 0.15f
                            ),
                            Color.Transparent                 // fade out
                        )
                    )
                )
        )

        if (countryCodes == null) {
            Loading()
        } else {
            CountryCodeList(
                currentItem = currentItem,
                items = filteredCountry.value!!,
                onCountryClick = onCountrySelected,
                countryFlags = RegisterActivityModel.Companion.countryFlags
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun Preview_IntelCodeList() {
    val nightMode = isSystemInDarkTheme()

    val countries = listOf(
        CountryCode("US", "United States", "+1"),
        CountryCode("CA", "Canada", "+1"),
        CountryCode("MX", "Mexico", "+52"),
        CountryCode("BR", "Brazil", "+55"),
        CountryCode("AR", "Argentina", "+54"),
        CountryCode("CL", "Chile", "+56"),
        CountryCode("CO", "Colombia", "+57"),
        CountryCode("PE", "Peru", "+51"),
        CountryCode("VE", "Venezuela", "+58"),
        CountryCode("UY", "Uruguay", "+598"),
    )

    ComposeAppTheme() {
        IntelCodeList(
            countryCodes = countries,
            currentItem = countries[0],
            nightMode = nightMode,
            onCountrySelected = {
            }
        )
    }
}
