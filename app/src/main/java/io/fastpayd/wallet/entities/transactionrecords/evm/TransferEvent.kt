package io.fastpayd.wallet.entities.transactionrecords.evm

import io.fastpayd.wallet.entities.TransactionValue

data class TransferEvent(
    val address: String?,
    val value: TransactionValue
)
