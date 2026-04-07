package io.fastpayd.wallet.entities.transactionrecords.tron

import io.fastpayd.wallet.entities.TransactionValue
import io.fastpayd.wallet.modules.transactions.TransactionSource
import io.horizontalsystems.marketkit.models.Token
import io.fastpayd.tronkit.models.Transaction

class TronApproveTransactionRecord(
    transaction: Transaction,
    baseToken: Token,
    source: TransactionSource,
    val spender: String,
    val value: TransactionValue
) : TronTransactionRecord(transaction, baseToken, source) {

    override val mainValue = value

}
