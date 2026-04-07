package io.fastpayd.wallet.entities.transactionrecords.evm

import io.fastpayd.wallet.modules.transactions.TransactionSource
import io.fastpayd.ethereumkit.models.Transaction
import io.horizontalsystems.marketkit.models.Token

class ContractCreationTransactionRecord(
    transaction: Transaction,
    baseToken: Token,
    source: TransactionSource
) : EvmTransactionRecord(transaction, baseToken, source)
