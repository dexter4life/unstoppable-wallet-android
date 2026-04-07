package io.fastpayd.wallet.modules.xtransaction.cells

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.fastpayd.wallet.R
import io.fastpayd.wallet.core.slideFromRight
import io.fastpayd.wallet.core.stats.StatEvent
import io.fastpayd.wallet.core.stats.StatPage
import io.fastpayd.wallet.core.stats.stat
import io.fastpayd.wallet.entities.TransactionValue
import io.fastpayd.wallet.modules.coin.CoinFragment
import io.fastpayd.wallet.modules.xtransaction.helpers.TransactionInfoHelper
import io.fastpayd.wallet.modules.xtransaction.helpers.coinAmountString
import io.fastpayd.wallet.modules.xtransaction.helpers.coinIconPainter
import io.fastpayd.wallet.modules.xtransaction.helpers.fiatAmountString
import io.fastpayd.wallet.ui.compose.ComposeAppTheme
import io.fastpayd.wallet.ui.compose.components.HFillSpacer
import io.fastpayd.wallet.ui.compose.components.HSpacer
import io.fastpayd.wallet.ui.compose.components.VSpacer
import io.fastpayd.wallet.ui.compose.components.caption_grey
import io.fastpayd.wallet.ui.compose.components.cell.CellUniversal
import io.fastpayd.wallet.ui.compose.components.subhead2_grey
import io.fastpayd.wallet.ui.compose.components.subhead2_leah

@Composable
fun AmountCell(
    title: String,
    coinIcon: Painter,
    coinProtocolType: String,
    coinAmount: String,
    coinAmountColor: Color,
    fiatAmount: String?,
    onClick: () -> Unit,
    borderTop: Boolean = true
) {
    CellUniversal(
        borderTop = borderTop,
        onClick = onClick
    ) {
        Image(
            painter = coinIcon,
            contentDescription = null,
            modifier = Modifier.size(32.dp),
            colorFilter = null,
            contentScale = ContentScale.FillBounds
        )

        HSpacer(16.dp)
        Column {
            subhead2_leah(text = title)
            VSpacer(height = 1.dp)
            caption_grey(text = coinProtocolType)
        }
        HFillSpacer(minWidth = 8.dp)
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = coinAmount,
                style = ComposeAppTheme.typography.subhead,
                color = coinAmountColor,
            )

            fiatAmount?.let {
                VSpacer(height = 1.dp)
                subhead2_grey(text = it)
            }
        }
    }
}

@Composable
fun AmountCellTV(
    title: String,
    transactionValue: TransactionValue,
    coinAmountColor: AmountColor,
    coinAmountSign: AmountSign,
    transactionInfoHelper: TransactionInfoHelper,
    navController: NavController,
    statPage: StatPage,
    borderTop: Boolean = true
) {
    AmountCell(
        title = title,
        coinIcon = coinIconPainter(
            url = transactionValue.coinIconUrl,
            alternativeUrl = transactionValue.alternativeCoinIconUrl,
            placeholder = transactionValue.coinIconPlaceholder
        ),
        coinProtocolType = transactionValue.badge
            ?: stringResource(id = R.string.CoinPlatforms_Native),
        coinAmount = coinAmountString(
            value = transactionValue.decimalValue?.abs(),
            coinCode = transactionValue.coinCode,
            coinDecimals = transactionValue.decimals,
            sign = coinAmountSign.sign()
        ),
        coinAmountColor = coinAmountColor.color(),
        fiatAmount = fiatAmountString(
            value = transactionInfoHelper.getXRate(transactionValue.coinUid)
                ?.let {
                    transactionValue.decimalValue?.abs()
                        ?.multiply(it)
                },
            fiatSymbol = transactionInfoHelper.getCurrencySymbol()
        ),
        onClick = {
            navController.slideFromRight(
                R.id.coinFragment,
                CoinFragment.Input(transactionValue.coinUid)
            )

            stat(
                page = statPage,
                event = StatEvent.OpenCoin(transactionValue.coinUid)
            )
        },
        borderTop = borderTop,
    )
}

enum class AmountSign {
    Plus, Minus, None;

    fun sign() = when (this) {
        Plus -> "+"
        Minus -> "-"
        None -> ""
    }
}

enum class AmountColor {
    Positive, Negative, Neutral;

    @Composable
    fun color() = when (this) {
        Positive -> ComposeAppTheme.colors.remus
        Negative -> ComposeAppTheme.colors.leah
        Neutral -> ComposeAppTheme.colors.leah
    }
}