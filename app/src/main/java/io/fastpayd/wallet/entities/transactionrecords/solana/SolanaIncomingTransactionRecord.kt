package io.fastpayd.wallet.entities.transactionrecords.solana

import io.fastpayd.wallet.entities.TransactionValue
import io.fastpayd.wallet.modules.transactions.TransactionSource
import io.horizontalsystems.marketkit.models.Token
import io.fastpayd.solanakit.models.Transaction

class SolanaIncomingTransactionRecord(
        transaction: Transaction,
        baseToken: Token,
        source: TransactionSource,
        val from: String?,
        val value: TransactionValue
): SolanaTransactionRecord(transaction, baseToken, source) {

    override val mainValue = value

}