package io.horizontalsystems.bankwallet.ui.compose.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.core.providers.resource.Country
import io.horizontalsystems.bankwallet.modules.authentication.register.RegisterActivityModel
import io.horizontalsystems.bankwallet.modules.coin.overview.ui.Loading
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme

@Composable
fun CountryListView(
    countries: List<Country>?,
    currentItem: Country? = null,
    onCountrySelected: (countryCode: String) -> Unit,
    nightMode: Boolean,
    onSelectChange: (String) -> Unit = {},
    value: String = ""
) {
    val focusRequester = remember { FocusRequester() }
    val backgroundColor =
        if (nightMode) ComposeAppTheme.colors.dark800 else ComposeAppTheme.colors.gray50
    val currentCountry = remember { mutableStateOf(value) }


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
                Text(
                    text = stringResource(R.string.AvailableCountry_Title),
                    modifier = Modifier.padding(end = 26.dp),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (nightMode) ComposeAppTheme.colors.white
                    else ComposeAppTheme.colors.dark
                )
                Spacer(Modifier.height(7.dp))
                BasicTextField(
                    value = currentCountry.value,
                    onValueChange = { text -> onSelectChange(text) },
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

        if (countries == null) {
            Loading()
        } else {
            CountryList(
                currentItem = currentItem,
                items = countries,
                onCountryClick = onCountrySelected,
                countryFlags = RegisterActivityModel.Companion.countryFlags
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun Preview_CountryListView() {
    val nightMode = isSystemInDarkTheme()
    val viewModel = RegisterActivityModel(null, null)


    ComposeAppTheme() {
        CountryListView(
            countries = viewModel.filteredCountries.value,
            currentItem = viewModel.countryData.value?.currentCountry,
            nightMode = nightMode,
            onCountrySelected = {
            }
        )
    }
}
