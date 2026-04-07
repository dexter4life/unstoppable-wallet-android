package io.fastpayd.wallet.modules.xtransaction.sections

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import io.fastpayd.wallet.R
import io.fastpayd.wallet.core.stats.StatPage
import io.fastpayd.wallet.core.stats.StatSection
import io.fastpayd.wallet.entities.TransactionValue
import io.fastpayd.wallet.modules.xtransaction.cells.AddressCell
import io.fastpayd.wallet.modules.xtransaction.cells.AmountCellTV
import io.fastpayd.wallet.modules.xtransaction.cells.AmountColor
import io.fastpayd.wallet.modules.xtransaction.cells.AmountSign
import io.fastpayd.wallet.modules.xtransaction.cells.TitleAndValueCell
import io.fastpayd.wallet.modules.xtransaction.helpers.TransactionInfoHelper
import io.fastpayd.wallet.ui.compose.components.cell.SectionUniversalLawrence
import io.horizontalsystems.marketkit.models.BlockchainType

@Composable
fun TransferCoinSection(
    amountTitle: String,
    transactionValue: TransactionValue,
    coinAmountColor: AmountColor,
    coinAmountSign: AmountSign,
    addressTitle: String,
    address: String,
    comment: String?,
    statPage: StatPage,
    addressStatSection: StatSection,
    navController: NavController,
    transactionInfoHelper: TransactionInfoHelper,
    blockchainType: BlockchainType,
) {
    SectionUniversalLawrence {
        AmountCellTV(
            title = amountTitle,
            transactionValue = transactionValue,
            coinAmountColor = coinAmountColor,
            coinAmountSign = coinAmountSign,
            transactionInfoHelper = transactionInfoHelper,
            navController = navController,
            statPage = statPage,
            borderTop = false
        )

        val contact = transactionInfoHelper.getContact(address, blockchainType)

        AddressCell(
            title = addressTitle,
            value = address,
            showAddContactButton = contact == null,
            blockchainType = blockchainType,
            statPage = statPage,
            statSection = addressStatSection,
            navController = navController
        )
        contact?.let {
            TitleAndValueCell(
                title = stringResource(R.string.TransactionInfo_ContactName),
                value = it.name
            )
        }
        comment?.let {
            TitleAndValueCell(
                title = stringResource(R.string.TransactionInfo_Memo),
                value = it
            )
        }
    }
}