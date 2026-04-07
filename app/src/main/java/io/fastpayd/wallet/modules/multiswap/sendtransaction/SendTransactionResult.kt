package io.fastpayd.wallet.modules.multiswap.sendtransaction

import io.fastpayd.wallet.entities.transactionrecords.bitcoin.BitcoinTransactionRecord
import io.fastpayd.ethereumkit.models.FullTransaction

sealed class SendTransactionResult {
    data class Evm(val fullTransaction: FullTransaction) : SendTransactionResult()
    data class Btc(val transactionRecord: BitcoinTransactionRecord?) : SendTransactionResult()
}
