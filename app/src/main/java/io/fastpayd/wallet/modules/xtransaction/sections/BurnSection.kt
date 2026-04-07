package io.fastpayd.wallet.modules.xtransaction.sections

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import io.fastpayd.wallet.R
import io.fastpayd.wallet.core.stats.StatPage
import io.fastpayd.wallet.entities.TransactionValue
import io.fastpayd.wallet.modules.xtransaction.cells.AmountCellTV
import io.fastpayd.wallet.modules.xtransaction.cells.AmountColor
import io.fastpayd.wallet.modules.xtransaction.cells.AmountSign
import io.fastpayd.wallet.modules.xtransaction.helpers.TransactionInfoHelper
import io.fastpayd.wallet.ui.compose.components.cell.SectionUniversalLawrence

@Composable
fun BurnSection(
    transactionValue: TransactionValue,
    transactionInfoHelper: TransactionInfoHelper,
    navController: NavController,
) {
    SectionUniversalLawrence {
        AmountCellTV(
            title = stringResource(R.string.Send_Confirmation_Burn),
            transactionValue = transactionValue,
            coinAmountColor = AmountColor.Negative,
            coinAmountSign = AmountSign.Minus,
            transactionInfoHelper = transactionInfoHelper,
            navController = navController,
            statPage = StatPage.TonConnect,
            borderTop = false,
        )
    }
}
