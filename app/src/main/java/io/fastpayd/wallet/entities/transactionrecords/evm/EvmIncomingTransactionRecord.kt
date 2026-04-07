package io.fastpayd.wallet.entities.transactionrecords.evm

import io.fastpayd.wallet.entities.TransactionValue
import io.fastpayd.wallet.modules.transactions.TransactionSource
import io.fastpayd.ethereumkit.models.Transaction
import io.horizontalsystems.marketkit.models.Token

class EvmIncomingTransactionRecord(
    transaction: Transaction,
    baseToken: Token,
    source: TransactionSource,
    val from: String,
    val value: TransactionValue,
    isSpam: Boolean
) : EvmTransactionRecord(
    transaction = transaction,
    baseToken = baseToken,
    source = source,
    foreignTransaction = true,
    spam = isSpam
) {

    override val mainValue = value

}
