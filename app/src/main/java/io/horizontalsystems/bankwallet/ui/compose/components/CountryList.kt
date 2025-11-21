package io.horizontalsystems.bankwallet.ui.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.core.alternativeImageUrl
import io.horizontalsystems.bankwallet.core.iconPlaceholder
import io.horizontalsystems.bankwallet.core.imageUrl
import io.horizontalsystems.bankwallet.modules.market.MarketViewItem
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme
import kotlinx.coroutines.launch


@Composable
fun CountryList(
    listState: LazyListState = rememberLazyListState(),
    items: List<MarketViewItem>,
    scrollToTop: Boolean,
    onAddFavorite: (String) -> Unit,
    onRemoveFavorite: (String) -> Unit,
    onCoinClick: (String) -> Unit,
    userScrollEnabled: Boolean = true,
    preItems: LazyListScope.() -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(state = listState, userScrollEnabled = userScrollEnabled) {
        preItems.invoke(this)
        itemsIndexed(items, key = { _, item -> item.coinUid }) { _, item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 24.dp)
                    .clickable { onCoinClick.invoke(item.fullCoin.coin.uid) }
                    .background(ComposeAppTheme.colors.tyler)
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                HsImage(
                    url = item.fullCoin.coin.imageUrl,
                    alternativeUrl = item.fullCoin.coin.alternativeImageUrl,
                    placeholder = item.fullCoin.iconPlaceholder,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(32.dp)
                        .clip(CircleShape)
                )
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    MarketCoinFirstRow(item.fullCoin.coin.code, item.value, item.signal)
                }
                HSpacer(16.dp)
                if (item.favorited) {
                    HsIconButton(
                        modifier = Modifier.size(20.dp),
                        onClick = {
                            onRemoveFavorite(item.coinUid)
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_heart_filled_20),
                            contentDescription = "heart icon button",
                            tint = ComposeAppTheme.colors.jacob
                        )
                    }
                } else {
                    HsIconButton(
                        modifier = Modifier.size(20.dp),
                        onClick = {
                            onAddFavorite(item.coinUid)
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_heart_20),
                            contentDescription = "heart icon button",
                            tint = ComposeAppTheme.colors.grey
                        )
                    }
                }
            }
            HsDivider()
        }
        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
        if (scrollToTop) {
            coroutineScope.launch {
                listState.scrollToItem(0)
            }
        }
    }
}
