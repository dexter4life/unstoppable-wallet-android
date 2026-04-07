package io.fastpayd.wallet.entities.transactionrecords.evm

import io.fastpayd.wallet.entities.TransactionValue
import io.fastpayd.wallet.modules.transactions.TransactionSource
import io.fastpayd.ethereumkit.models.Transaction
import io.horizontalsystems.marketkit.models.Token

class UnknownSwapTransactionRecord(
    transaction: Transaction,
    baseToken: Token,
    source: TransactionSource,
    val exchangeAddress: String,
    val valueIn: TransactionValue?,
    val valueOut: TransactionValue?,
) : EvmTransactionRecord(transaction, baseToken, source)
