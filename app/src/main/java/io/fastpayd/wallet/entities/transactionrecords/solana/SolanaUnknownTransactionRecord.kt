package io.fastpayd.wallet.entities.transactionrecords.solana

import io.fastpayd.wallet.modules.transactions.TransactionSource
import io.horizontalsystems.marketkit.models.Token
import io.fastpayd.solanakit.models.Transaction

class SolanaUnknownTransactionRecord(
        transaction: Transaction,
        baseToken: Token,
        source: TransactionSource,
        val incomingTransfers: List<Transfer>,
        val outgoingTransfers: List<Transfer>
): SolanaTransactionRecord(transaction, baseToken, source)
