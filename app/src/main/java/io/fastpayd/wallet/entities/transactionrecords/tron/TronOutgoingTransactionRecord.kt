package io.fastpayd.wallet.entities.transactionrecords.tron

import io.fastpayd.wallet.entities.TransactionValue
import io.fastpayd.wallet.modules.transactions.TransactionSource
import io.horizontalsystems.marketkit.models.Token
import io.fastpayd.tronkit.models.Transaction

class TronOutgoingTransactionRecord(
    transaction: Transaction,
    baseToken: Token,
    source: TransactionSource,
    val to: String,
    val value: TransactionValue,
    val sentToSelf: Boolean
) : TronTransactionRecord(transaction, baseToken, source) {

    override val mainValue = value

}
