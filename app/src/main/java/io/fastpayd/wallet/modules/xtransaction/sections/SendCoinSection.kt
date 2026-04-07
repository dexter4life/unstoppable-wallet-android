package io.fastpayd.wallet.modules.xtransaction.sections

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import io.fastpayd.wallet.R
import io.fastpayd.wallet.core.stats.StatPage
import io.fastpayd.wallet.core.stats.StatSection
import io.fastpayd.wallet.entities.TransactionValue
import io.fastpayd.wallet.modules.xtransaction.cells.AmountColor
import io.fastpayd.wallet.modules.xtransaction.cells.AmountSign
import io.fastpayd.wallet.modules.xtransaction.helpers.TransactionInfoHelper
import io.horizontalsystems.marketkit.models.BlockchainType

@Composable
fun SendCoinSection(
    transactionValue: TransactionValue,
    address: String,
    comment: String?,
    sentToSelf: Boolean,
    statPage: StatPage,
    navController: NavController,
    transactionInfoHelper: TransactionInfoHelper,
    blockchainType: BlockchainType
) {
    TransferCoinSection(
        amountTitle = stringResource(R.string.Send_Confirmation_YouSend),
        transactionValue = transactionValue,
        coinAmountColor = AmountColor.Negative,
        coinAmountSign = if (sentToSelf) AmountSign.None else AmountSign.Minus,
        addressTitle = stringResource(R.string.TransactionInfo_To),
        address = address,
        comment = comment,
        statPage = statPage,
        addressStatSection = StatSection.AddressTo,
        navController = navController,
        transactionInfoHelper = transactionInfoHelper,
        blockchainType = blockchainType,
    )
}