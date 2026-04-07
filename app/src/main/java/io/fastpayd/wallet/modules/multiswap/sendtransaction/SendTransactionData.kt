package io.fastpayd.wallet.modules.multiswap.sendtransaction

import io.fastpayd.wallet.R
import io.fastpayd.wallet.entities.CoinValue
import io.fastpayd.bitcoincore.storage.UtxoFilters
import io.fastpayd.ethereumkit.models.TransactionData
import java.math.BigDecimal

sealed class SendTransactionData {
    data class Evm(
        val transactionData: TransactionData,
        val gasLimit: Long?,
        val feesMap: Map<FeeType, CoinValue> = mapOf()
    ): SendTransactionData()

    data class Btc(
        val address: String,
        val memo: String,
        val amount: BigDecimal,
        val recommendedGasRate: Int,
        val dustThreshold: Int?,
        val changeToFirstInput: Boolean,
        val utxoFilters: UtxoFilters,
        val feesMap: Map<FeeType, CoinValue>
    ) : SendTransactionData()
}

enum class FeeType(val stringResId: Int) {
    Outbound(R.string.Fee_OutboundFee),
    Liquidity(R.string.Fee_LiquidityFee);
}
