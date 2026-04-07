package io.fastpayd.wallet.entities.transactionrecords.solana

import io.fastpayd.wallet.entities.TransactionValue
import io.fastpayd.wallet.modules.transactions.TransactionSource
import io.horizontalsystems.marketkit.models.Token
import io.fastpayd.solanakit.models.Transaction

class SolanaOutgoingTransactionRecord(
        transaction: Transaction,
        baseToken: Token,
        source: TransactionSource,
        val to: String?,
        val value: TransactionValue,
        val sentToSelf: Boolean
): SolanaTransactionRecord(transaction, baseToken, source) {

    override val mainValue = value

}
