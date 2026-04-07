package io.fastpayd.wallet.entities.transactionrecords.evm

import io.fastpayd.wallet.entities.TransactionValue
import io.fastpayd.wallet.modules.transactions.TransactionSource
import io.fastpayd.ethereumkit.models.Transaction
import io.horizontalsystems.marketkit.models.Token

class ExternalContractCallTransactionRecord(
    transaction: Transaction,
    baseToken: Token,
    source: TransactionSource,
    val incomingEvents: List<TransferEvent>,
    val outgoingEvents: List<TransferEvent>,
    isSpam: Boolean
) : EvmTransactionRecord(
    transaction = transaction,
    baseToken = baseToken,
    source = source,
    foreignTransaction = true,
    spam = isSpam
) {

    override val mainValue: TransactionValue?
        get() {
            val (incomingValues, outgoingValues) = combined(incomingEvents, outgoingEvents)

            return when {
                (incomingValues.isEmpty() && outgoingValues.size == 1) -> outgoingValues.first()
                (incomingValues.size == 1 && outgoingValues.isEmpty()) -> incomingValues.first()
                else -> null
            }
        }
}
