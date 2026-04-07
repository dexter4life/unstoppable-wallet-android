package io.fastpayd.wallet.modules.xtransaction.sections.ton

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import io.fastpayd.wallet.R
import io.fastpayd.wallet.core.stats.StatPage
import io.fastpayd.wallet.core.stats.StatSection
import io.fastpayd.wallet.entities.TransactionValue
import io.fastpayd.wallet.modules.transactions.TransactionViewItem
import io.fastpayd.wallet.modules.xtransaction.cells.AddressCell
import io.fastpayd.wallet.modules.xtransaction.cells.AmountCellTV
import io.fastpayd.wallet.modules.xtransaction.cells.AmountColor
import io.fastpayd.wallet.modules.xtransaction.cells.AmountSign
import io.fastpayd.wallet.modules.xtransaction.cells.HeaderCell
import io.fastpayd.wallet.modules.xtransaction.helpers.TransactionInfoHelper
import io.fastpayd.wallet.ui.compose.components.cell.SectionUniversalLawrence
import io.horizontalsystems.marketkit.models.BlockchainType

@Composable
fun ContractCallSection(
    navController: NavController,
    operation: String,
    address: String,
    transactionValue: TransactionValue,
    transactionInfoHelper: TransactionInfoHelper,
    blockchainType: BlockchainType,
) {
    SectionUniversalLawrence {
        HeaderCell(
            title = stringResource(R.string.Transactions_ContractCall),
            value = operation,
            painter = TransactionViewItem.Icon.Platform(blockchainType).iconRes?.let {
                painterResource(it)
            }
        )
        val contact = transactionInfoHelper.getContact(address, blockchainType)
        AddressCell(
            title = stringResource(R.string.TransactionInfo_To),
            value = address,
            showAddContactButton = contact == null,
            blockchainType = blockchainType,
            statPage = StatPage.TonConnect,
            statSection = StatSection.AddressTo,
            navController = navController
        )

        AmountCellTV(
            title = stringResource(R.string.Send_Confirmation_YouSend),
            transactionValue = transactionValue,
            coinAmountColor = AmountColor.Negative,
            coinAmountSign = AmountSign.Minus,
            transactionInfoHelper = transactionInfoHelper,
            navController = navController,
            statPage = StatPage.TonConnect
        )
    }
}