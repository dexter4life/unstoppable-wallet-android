package io.fastpayd.wallet.entities.transactionrecords.evm

import io.fastpayd.wallet.entities.TransactionValue
import io.fastpayd.wallet.modules.transactions.TransactionSource
import io.fastpayd.ethereumkit.models.Transaction
import io.horizontalsystems.marketkit.models.Token

class ApproveTransactionRecord(
    transaction: Transaction,
    baseToken: Token,
    source: TransactionSource,
    val spender: String,
    val value: TransactionValue
) : EvmTransactionRecord(transaction, baseToken, source) {

    override val mainValue = value

}
