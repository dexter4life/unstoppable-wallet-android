package io.fastpayd.wallet.core.managers

import io.fastpayd.wallet.core.adapters.TonTransactionRecord
import io.fastpayd.wallet.modules.transactionInfo.TransactionInfoViewItem
import io.horizontalsystems.marketkit.models.BlockchainType

object TonHelper {
    fun getViewItemsForAction(
        action: TonTransactionRecord.Action,
        rates: Map<String, Any>,
        blockchainType: BlockchainType,
        hideAmount: Boolean,
        showDetails: Boolean,
    ): List<TransactionInfoViewItem> = listOf()
}