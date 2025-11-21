package io.horizontalsystems.bankwallet.ui.compose.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.modules.market.MarketDataValue
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme
import io.horizontalsystems.marketkit.models.Analytics.TechnicalAdvice.Advice


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CountryCell(
    countryName: String,
    countryFlagUrl: String,
    alternativeCountryFlagUrl: String? = null,
    countryIconPlaceholder: Int,
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 24.dp)
            .combinedClickable(
                enabled = onClick != null || onLongClick != null,
                onClick = onClick ?: { },
                onLongClick = onLongClick
            )
            .background(ComposeAppTheme.colors.tyler)
            .padding(horizontal = 16.dp)
            .padding(vertical = 12.dp),
        verticalAlignment =  Alignment.CenterVertically,
    ) {
        FlagImage(
            url = countryFlagUrl,
            alternativeUrl = alternativeCountryFlagUrl,
            placeholder = countryIconPlaceholder,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
        )
        Text(
            text = countryName,
            modifier = Modifier.padding(start = 5.dp),
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Clip,
            maxLines = Int.MAX_VALUE,
            style = ComposeAppTheme.typography.headline2,
            color = ComposeAppTheme.colors.leah,
        )
    }
}

@Preview
@Composable
fun PreviewCountry(){
    ComposeAppTheme {
        CountryCell(
            countryName = "United States",
            countryFlagUrl = "https://cdn.blocksdecoded.com/auditor-icons/ABDK@2x.png",
            countryIconPlaceholder = R.drawable.ic_globe_20
        )
    }
}

