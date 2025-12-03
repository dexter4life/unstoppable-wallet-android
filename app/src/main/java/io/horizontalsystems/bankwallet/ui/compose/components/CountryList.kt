package io.horizontalsystems.bankwallet.ui.compose.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.core.providers.resource.Country
import io.horizontalsystems.bankwallet.core.providers.resource.CountryFlag
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme

@Composable
private fun CountryItem(item: Country, flag: CountryFlag?, onCountryClick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCountryClick.invoke(item.code) }
            .padding(horizontal = 16.dp)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
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
            modifier = Modifier.weight(1f),
            fontSize = 18.sp,
            color = ComposeAppTheme.colors.leah
        )
        Icon(
            painter = painterResource(R.drawable.ic_arrow_right),
            contentDescription = null,
            tint = ComposeAppTheme.colors.grey,
            modifier = Modifier.size(22.dp)
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
            androidx.compose.material.Icon(
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
fun CountryList(
    listState: LazyListState = rememberLazyListState(),
    currentItem: Country? = null,
    countryFlags: List<CountryFlag>,
    items: List<Country>,
    onCountryClick: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(top = 8.dp)
    ) {
        when {
            items.isEmpty() -> NoCountry()
            currentItem != null -> {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = stringResource(R.string.CountryList_CurrentLocationLabel),
                        fontSize = 15.sp,
                        color = ComposeAppTheme.colors.grey,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                    )
                    CountryItem(
                        currentItem,
                        countryFlags.find { flag -> currentItem.code.equals(flag.code, true) },
                        onCountryClick,
                    )
                }
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = stringResource(R.string.CountryList_OtherCountryLabel),
                        fontSize = 15.sp,
                        color = ComposeAppTheme.colors.grey,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(top = 10.dp),
                    )
                    LazyColumn(state = listState, userScrollEnabled = true) {
                        itemsIndexed(items) { _, item ->
                            val flag =
                                countryFlags.find { flag -> flag.code.equals(item.code, true) }
                            CountryItem(item, flag, onCountryClick)
                            Divider(
                                thickness = 0.5.dp,
                                color = ComposeAppTheme.colors.grey,
                                modifier = Modifier.padding(horizontal = 5.dp)
                            )
                        }
                    }
                }
            }

            else -> {
                LazyColumn(state = listState, userScrollEnabled = true) {
                    itemsIndexed(items) { _, item ->
                        val flag = countryFlags.find { flag -> flag.code.equals(item.code, true) }
                        CountryItem(item, flag, onCountryClick)
                        Divider(
                            thickness = 0.5.dp,
                            color = ComposeAppTheme.colors.grey,
                            modifier = Modifier.padding(horizontal = 5.dp)
                        )
                    }
                }
            }
        }
    }
}
