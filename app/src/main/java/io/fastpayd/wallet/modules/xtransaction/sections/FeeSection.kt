package io.fastpayd.wallet.modules.xtransaction.sections

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import io.fastpayd.wallet.entities.CurrencyValue
import io.fastpayd.wallet.entities.TransactionValue
import io.fastpayd.wallet.modules.amount.AmountInputType
import io.fastpayd.wallet.modules.fee.HSFeeRaw
import io.fastpayd.wallet.modules.xtransaction.helpers.TransactionInfoHelper
import io.fastpayd.wallet.ui.compose.components.cell.SectionUniversalLawrence

@Composable
fun FeeSection(
    transactionInfoHelper: TransactionInfoHelper,
    fee: TransactionValue.CoinValue,
    navController: NavController,
) {
    SectionUniversalLawrence {
        val rateCurrencyValue = transactionInfoHelper.getXRate(fee.coinUid)?.let {
            CurrencyValue(
                currency = transactionInfoHelper.getCurrency(),
                value = it
            )
        }
        HSFeeRaw(
            coinCode = fee.coinCode,
            coinDecimal = fee.decimals,
            fee = fee.value,
            amountInputType = AmountInputType.COIN,
            rate = rateCurrencyValue,
            navController = navController
        )
    }
}